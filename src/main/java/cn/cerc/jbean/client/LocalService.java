package cn.cerc.jbean.client;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.cerc.jbean.core.Application;
import cn.cerc.jbean.core.IService;
import cn.cerc.jbean.core.IStatus;
import cn.cerc.jbean.core.ServerConfig;
import cn.cerc.jbean.core.ServiceStatus;
import cn.cerc.jbean.other.BufferType;
import cn.cerc.jbean.other.MemoryBuffer;
import cn.cerc.jbean.tools.MD5;
import cn.cerc.jdb.cache.IMemcache;
import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.core.Record;

public class LocalService implements IServiceProxy {
	private static final Logger log = Logger.getLogger(LocalService.class);
	private String serviceCode;

	private String message;
	private IHandle handle;
	// 是否激活缓存
	private boolean bufferRead = true;
	private boolean bufferWrite = true;

	private DataSet dataIn = new DataSet();
	private DataSet dataOut = new DataSet();

	public LocalService(IHandle handle) {
		this.handle = handle;
		if (handle == null)
			throw new RuntimeException("handle is null.");

		String pageNo = null;
		HttpServletRequest req = (HttpServletRequest) handle.getProperty("request");
		if (req != null)
			pageNo = (String) req.getParameter("pageno");

		// 遇到分页符时，尝试读取缓存
		this.bufferRead = pageNo != null;
	}

	public LocalService(IHandle handle, String service) {
		this(handle);
		this.setService(service);
	}

	public String getService() {
		return serviceCode;
	}

	public IServiceProxy setService(String service) {
		this.serviceCode = service;
		return this;
	}

	public String getMessage() {
		return message.replaceAll("'", "\"");
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// 带缓存调用服务
	@Override
	public boolean exec(Object... args) {
		if (args.length > 0) {
			Record headIn = getDataIn().getHead();
			if (args.length % 2 != 0)
				throw new RuntimeException("传入的参数数量必须为偶数！");
			for (int i = 0; i < args.length; i = i + 2)
				headIn.setField(args[i].toString(), args[i + 1]);
		}
		if (handle == null)
			throw new RuntimeException("handle is null.");
		if (serviceCode == null)
			throw new RuntimeException("service is null.");

		IService bean = Application.getService(handle, serviceCode);
		if (bean == null) {
			this.message = String.format("bean %s not find", serviceCode);
			return false;
		}

		try {
			log.info(this.serviceCode);
			if (ServerConfig.getAppLevel() == ServerConfig.appRelease) {
				IStatus status = bean.execute(dataIn, dataOut);
				boolean result = status.getResult();
				message = status.getMessage();
				return result;
			}

			IMemcache buff = Application.getMemcache();
			// 制作临时缓存Key
			String key = MD5.get(handle.getUserCode() + this.serviceCode + dataIn.getJSON());

			if (bufferRead) {
				String buffValue = (String) buff.get(key);
				if (buffValue != null) {
					log.debug("read from buffer: " + this.serviceCode);
					dataOut.setJSON(buffValue);
					message = dataOut.getHead().getString("_message_");
					return dataOut.getHead().getBoolean("_result_");
				}
			}

			// 没有缓存时，直接读取并存入缓存
			bean.init(handle);
			IStatus status = bean.execute(dataIn, dataOut);
			boolean result = status.getResult();
			message = status.getMessage();

			if (bufferWrite) {
				log.debug("write to buffer: " + this.serviceCode);
				dataOut.getHead().setField("_message_", message);
				dataOut.getHead().setField("_result_", result);
				buff.set(key, dataOut.getJSON());
			}
			return result;
		} catch (Exception e) {
			Throwable err = e;
			if (e.getCause() != null)
				err = e.getCause();
			log.error(err.getMessage(), err);
			message = err.getMessage();
			return false;
		}
	}

	// 不带缓存调用服务
	public IStatus execute(Object... args) {
		if (args.length > 0) {
			Record headIn = getDataIn().getHead();
			if (args.length % 2 != 0)
				return new ServiceStatus(false, "传入的参数数量必须为偶数！");
			for (int i = 0; i < args.length; i = i + 2)
				headIn.setField(args[i].toString(), args[i + 1]);
		}
		if (handle == null)
			return new ServiceStatus(false, "handle is null.");
		if (serviceCode == null)
			return new ServiceStatus(false, "service is null.");

		IService bean = Application.getService(handle, serviceCode);
		if (bean == null)
			return new ServiceStatus(false, String.format("bean %s not find", serviceCode));

		try {
			log.info(this.serviceCode);
			IStatus status = bean.execute(dataIn, dataOut);
			message = status.getMessage();
			return status;
		} catch (Exception e) {
			Throwable err = e;
			if (e.getCause() != null)
				err = e.getCause();
			log.error(err.getMessage(), err);
			message = err.getMessage();
			return new ServiceStatus(false, message);
		}
	}

	public DataSet getDataOut() {
		return this.dataOut;
	}

	public DataSet getDataIn() {
		return this.dataIn;
	}

	public String getExportKey() {
		String tmp = "" + System.currentTimeMillis();
		try (MemoryBuffer buff = new MemoryBuffer(BufferType.getExportKey, handle.getUserCode(), tmp)) {
			buff.setField("data", this.getDataIn().getJSON());
		}
		return tmp;
	}

	public LocalService setBufferRead(boolean bufferRead) {
		this.bufferRead = bufferRead;
		return this;
	}

	public LocalService setBufferWrite(boolean bufferWrite) {
		this.bufferWrite = bufferWrite;
		return this;
	}

	public static void listMethod(Class<?> clazz) {
		Map<String, Class<?>> items = new HashMap<>();
		String[] args = clazz.getName().split("\\.");
		String classCode = args[args.length - 1];
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getReturnType().getName().equals("boolean")) {
				if (method.getParameters().length == 0) {
					String name = method.getName();
					if (method.getName().startsWith("_"))
						name = name.substring(1, name.length());
					items.put(classCode + "." + name, clazz);
				}
			}
		}
		for (String key : items.keySet()) {
			log.info(key);
		}
	}

}
