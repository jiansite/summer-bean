package cn.cerc.jbean.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.cerc.jbean.form.IForm;
import cn.cerc.jbean.tools.IAppLogin;
import cn.cerc.jdb.cache.CacheConnection;
import cn.cerc.jdb.cache.IMemcache;
import cn.cerc.jdb.core.IConfig;
import cn.cerc.jdb.core.IHandle;

public class Application {
    private static String xmlFile = "classpath:application.xml";
    private static ApplicationContext app;
    private static AppConfig appConfig;
    private static ServerConfig serverConfig;

    private static ApplicationContext serviceItems;
    private static String serviceFile = "classpath:app-services.xml";
    private static ApplicationContext formItems;
    private static String formFile = "classpath:app-forms.xml";
    private static ApplicationContext reportItems;
    private static String reportFile = "classpath:app-report.xml";

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
    public static final String deviceLanguage = "language";

    // 签核代理用户列表，代理多个用户以半角逗号隔开
    public static final String ProxyUsers = "ProxyUsers";
    // 客户端代码
    public static final String clientIP = "clientIP";
    // 本地会话登录时间
    public static final String loginTime = "loginTime";
    // 浏览器通用客户设备Id
    public static final String webclient = "webclient";
    // 默认界面语言版本
    public static final String LangageDefault = "cn"; // 可选：cn/en

    @Deprecated // 请改使用getAppConfig()
    public static AppConfig getConfig() {
        init();
        return appConfig;
    }

    public static AppConfig getAppConfig() {
        init();
        return appConfig;
    }

    public static IConfig getServerConfig() {
        if (serverConfig == null)
            serverConfig = ServerConfig.getInstance();
        return serverConfig;
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

    public static boolean containsBean(String beanCode) {
        init();
        return app.containsBean(beanCode);
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

        formItems = getFormItems();
        if (!formItems.containsBean(formId)) {
            throw new RuntimeException(String.format("form %s not find!", formId));
        }

        IForm form = formItems.getBean(formId, IForm.class);
        form.setRequest(req);
        form.setResponse(resp);

        return form;
    }

    public static ApplicationContext getFormItems() {
        if (formItems == null)
            formItems = new FileSystemXmlApplicationContext(formFile);
        return formItems;
    }

    public static ApplicationContext getReportItems() {
        if (reportItems == null)
            reportItems = new FileSystemXmlApplicationContext(reportFile);
        return reportItems;
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

    private static void init() {
        if (app == null) {
            app = new FileSystemXmlApplicationContext(xmlFile);
            appConfig = app.getBean("AppConfig", AppConfig.class);
            if (appConfig == null)
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

    public static String getLangage() {
        init();
        String lang = getServerConfig().getProperty(deviceLanguage);
        if (lang == null || "".equals(lang) || LangageDefault.equals(lang))
            return LangageDefault;
        else if ("en".equals(lang))
            return lang;
        else
            throw new RuntimeException("not support language: " + lang);
    }

    public static void main(String[] args) {
        // listMethod(TAppLogin.class);
        serviceItems = new FileSystemXmlApplicationContext(serviceFile);
        for (String key : serviceItems.getBeanDefinitionNames()) {
            if (serviceItems.getBean(key) == null)
                System.out.println(key);
        }
    }
}
