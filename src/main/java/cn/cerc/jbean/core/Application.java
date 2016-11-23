package cn.cerc.jbean.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.cerc.jbean.form.IForm;
import cn.cerc.jbean.tools.IAppLogin;
import cn.cerc.jdb.cache.CacheConnection;
import cn.cerc.jdb.cache.IMemcache;
import cn.cerc.jdb.core.IHandle;

public class Application {
	private static String xmlFile = "classpath:application.xml";
	private static ApplicationContext app;
	private static AppConfig config;

	private static ApplicationContext serviceItems;
	private static String serviceFile = "classpath:app-services.xml";
	private static ApplicationContext formItems;
	private static String formFile = "classpath:app-forms.xml";

	// Tomcat JSESSION.ID
	public static final String sessionId = "sessionId";
	// token id
	public static final String token = "ID";
	// user id
	public static final String userId = "UserID";
	public static final String userCode = "UserCode";
	public static final String userName = "UserName";
	public static final String roleCode = "RoleCode";
	public static final String bookNo = "BookNo";
	// 签核代理用户列表，代理多个用户以半角逗号隔开
	public static final String ProxyUsers = "ProxyUsers";
	// 客户端代码
	public static final String clientIP = "clientIP";
	// 本地会话登录时间
	public static final String loginTime = "loginTime";
	// 浏览器通用客户设备Id
	public static final String webclient = "webclient";

	public static AppConfig getConfig() {
		init();
		return config;
	}

	public static IHandle getHandle() {
		init();
		if (!app.containsBean("AppHandle"))
			throw new RuntimeException(String.format("%s 中没有找到 bean: AppHandle", xmlFile));

		return app.getBean("AppHandle", IHandle.class);
	}

	public static IPassport getPassport(IHandle handle) {
		init();
		AbstractHandle bean = getBean("Passport", AbstractHandle.class);
		if (handle != null)
			bean.setHandle(handle);
		return (IPassport) bean;
	}

	public static <T> T getBean(String beanCode, Class<T> requiredType) {
		init();
		return app.getBean(beanCode, requiredType);
	}

	public static IService getService(IHandle handle, String serviceCode) {
		init();
		if (serviceItems == null)
			serviceItems = new FileSystemXmlApplicationContext(serviceFile);

		if (serviceItems.containsBean(serviceCode)) {
			IService bean = serviceItems.getBean(serviceCode, IService.class);
			if (handle != null)
				bean.init(handle);
			return bean;
		}
		return null;
	}

	public static IForm getForm(HttpServletRequest req, HttpServletResponse resp, String formId) {
		if (formId == null || formId.equals("") || formId.equals("service"))
			return null;

		init();

		if (formItems == null)
			formItems = new FileSystemXmlApplicationContext(formFile);

		if (!formItems.containsBean(formId))
			throw new RuntimeException(String.format("form %s not find!", formId));

		IForm form = formItems.getBean(formId, IForm.class);
		form.setRequest(req);
		form.setResponse(resp);

		return form;
	}

	public static ApplicationContext getServices() {
		init();
		if (serviceItems == null)
			serviceItems = new FileSystemXmlApplicationContext(serviceFile);
		return serviceItems;
	}

	public static IMemcache getMemcache() {
		init();
		if (!app.containsBean("CacheConnection"))
			throw new RuntimeException(String.format("%s 中没有找到 bean: Memcache", xmlFile));

		CacheConnection conn = app.getBean("CacheConnection", CacheConnection.class);
		return conn.getSession();
	}

	public static void main(String[] args) {
		// listMethod(TAppLogin.class);
		serviceItems = new FileSystemXmlApplicationContext(serviceFile);
		for (String key : serviceItems.getBeanDefinitionNames()) {
			if (serviceItems.getBean(key) == null)
				System.out.println(key);
		}
	}

	private static void init() {
		if (app == null) {
			app = new FileSystemXmlApplicationContext(xmlFile);
			config = app.getBean("AppConfig", AppConfig.class);
			if (config == null)
				throw new RuntimeException(String.format("%s 中没有找到 bean: AppConfig", xmlFile));
		}
	}

	public static IAppLogin getAppLogin(IForm form) {
		init();
		if (!app.containsBean("AppLogin"))
			throw new RuntimeException(String.format("%s 中没有找到 bean: AppLogin", xmlFile));
		IAppLogin result = app.getBean("AppLogin", IAppLogin.class);
		result.init(form);
		return result;
	}
}
