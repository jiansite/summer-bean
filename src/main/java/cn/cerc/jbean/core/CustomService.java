package cn.cerc.jbean.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import cn.cerc.jbean.other.SystemTable;
import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.core.TDateTime;

public class CustomService extends AbstractHandle implements IService, IRestful {
	private static final Logger log = Logger.getLogger(CustomService.class);
	private DataSet dataIn = null; // request
	private DataSet dataOut = null; // response
	private String message = "";
	private StringBuffer msg = null;
	private String funcCode;
	private String restPath;

	public CustomService() {
		super();
	}

	public CustomService(IHandle handle) {
		super();
		this.init(handle);
	}

	public CustomService(CustomService owner, boolean refData) {
		super();
		this.init(owner);
		if (refData) {
			this.dataIn = owner.getDataIn();
			this.dataOut = owner.getDataOut();
		}
	}

	@Override
	public void init(IHandle handle) {
		this.handle = handle;
	}

	public CustomService init(CustomService owner, boolean refData) {
		this.init(owner);
		if (refData) {
			this.dataIn = owner.getDataIn();
			this.dataOut = owner.getDataOut();
		}
		return this;
	}

	@Override
	public IStatus execute(DataSet dataIn, DataSet dataOut) {
		if (this.funcCode == null)
			throw new RuntimeException("funcCode is null");
		this.dataIn = dataIn;
		this.dataOut = dataOut;
		return this.exec(this.funcCode);
	}

	public IStatus exec(String func) {
		ServiceStatus ss = new ServiceStatus(false);
		Class<?> self = this.getClass();
		Method mt = null;
		for (Method item : self.getMethods()) {
			if (item.getName().equals(func)) {
				mt = item;
				break;
			}
		}
		if (mt == null) {
			this.setMessage(String.format("没有找到服务：%s.%s ！", this.getClass().getName(), func));
			ss.setMessage(this.getMessage());
			ss.setResult(false);
			return ss;
		}

		Webfunc webfunc = mt.getAnnotation(Webfunc.class);
		// if (webfunc == null)
		// log.warn("webfunc not define: " + self.getName() + "." + func);

		try {
			long startTime = System.currentTimeMillis();
			try {
				// 执行具体的服务函数
				ss.setResult((Boolean) mt.invoke(this));
				ss.setMessage(this.getMessage());
				return ss;
			} finally {
				if (dataOut != null)
					dataOut.first();
				long totalTime = System.currentTimeMillis() - startTime;
				long timeout = webfunc != null ? webfunc.timeout() : 1000;
				if (totalTime > timeout) {
					String tmp[] = this.getClass().getName().split("\\.");
					String service = tmp[tmp.length - 1] + "." + func;
					saveServiceTimeout(service, totalTime, false, false);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Throwable err = e.getCause() != null ? e.getCause() : e;
			if ((err instanceof ServiceException)) {
				this.setMessage(err.getMessage());
				ss.setMessage(err.getMessage());
				ss.setResult(false);
				return ss;
			} else {
				log.error(err.getMessage(), err);
				this.setMessage(err.getMessage());
				ss.setMessage(err.getMessage());
				ss.setResult(false);
				return ss;
			}
		}
	}

	private void saveServiceTimeout(String service, long totalTime, boolean trans, boolean client) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				String.format("insert into %s (CorpNo_,Service_,Trans_,TickCount_,AppUser_,AppDate_,Client_,DataIn_) ",
						SystemTable.get(SystemTable.getAppLogs)));
		sql.append(String.format("values ('%s','%s',%d,%s,'%s','%s',%d,'%s')", this.getCorpNo(), service, trans ? 1 : 0,
				"" + totalTime, this.getUserCode(), TDateTime.Now(), client ? 1 : 0,
				this.getDataIn().getJSON().replace("'", "`")));
		this.getConnection().execute(sql.toString());
	}

	public DataSet getDataIn() {
		if (dataIn == null)
			dataIn = new DataSet();
		return dataIn;
	}

	public DataSet getDataOut() {
		if (dataOut == null)
			dataOut = new DataSet();
		return dataOut;
	}

	// 需要返回的失败讯息, 且永远为 false !
	public boolean fail(String text) {
		this.setMessage(text);
		return false;
	}

	public StringBuffer getMsg() {
		if (msg == null)
			msg = new StringBuffer(message);
		return msg;
	}

	public String getMessage() {
		return msg != null ? msg.toString() : message;
	}

	public void setMessage(String message) {
		if (message == null || "".equals(message.trim()))
			return;
		if (msg != null)
			this.msg.append(message);
		else
			this.message = message;
	}

	@Override
	public String getJSON(DataSet dataOut) {
		return String.format("[%s]", this.getDataOut().getJSON());
	}

	// 设置是否需要授权才能登入
	@Override
	public boolean checkSecurity(IHandle handle) {
		IHandle sess = (IHandle) handle.getProperty(null);
		return sess != null ? sess.logon() : false;
	}

	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	@Override
	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}

	@Override
	public String getRestPath() {
		return restPath;
	}
}
