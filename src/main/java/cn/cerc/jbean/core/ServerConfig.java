package cn.cerc.jbean.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.cerc.jdb.core.IConfig;
import cn.cerc.jdb.other.utils;

public class ServerConfig implements IConfig {
    private static final Logger log = Logger.getLogger(ServerConfig.class);
    public static final String TaskServiceEnabled = "task.service";
    public static final String TaskServiceToken = "task.token";
    public static final String AdminMobile = "admin.mobile";
    public static final String AdminEmail = "admin.email";

    private static ServerConfig instance;
    private static Properties properties = new Properties();
    private Map<String, String> defaultParams = new HashMap<>();

    public static final int appNone = 0;
    public static final int appTest = 1;
    public static final int appBeta = 2;
    public static final int appRelease = 3;

    static {
        String confFile = "/application.properties";
        try {
            InputStream file = ServerConfig.class.getResourceAsStream(confFile);
            if (file != null) {
                properties.load(file);
                log.info("read from file: " + confFile);
            } else {
                log.error("not find file: " + confFile);
            }
        } catch (FileNotFoundException e) {
            log.error("The settings file '" + confFile + "' does not exist.");
        } catch (IOException e) {
            log.error("Failed to load the settings from the file: " + confFile);
        }
    }

    public ServerConfig() {
        if (instance != null) {
            log.error("ServerConfig instance is not null");
        }
        instance = this;
    }

    public static ServerConfig getInstance() {
        if (instance == null) {
            new ServerConfig();
        }
        return instance;
    }

    public static String getAppName() {
        String result = getInstance().getProperty("appName", "localhost");
        return result;
    }

    public static int getAppLevel() {
        String tmp = getInstance().getProperty("version", "beta");
        if ("test".equals(tmp))
            return 1;
        if ("beta".equals(tmp))
            return 2;
        if ("release".equals(tmp))
            return 3;
        else
            return 0;
    }

    public static int getDocLevel() {
        String level = getInstance().getProperty("docs.service", "0");
        return utils.strToIntDef(level, 0);
    }

    @Deprecated
    public static int getTimeoutWarn() {
        String str = getInstance().getProperty("timeout.warn", "60");
        return Integer.parseInt(str); // 默认60秒
    }

    public static String getAdminMobile() {
        return getInstance().getProperty(AdminMobile, null);
    }

    public static String getAdminEmail() {
        return getInstance().getProperty(AdminEmail, null);
    }

    // 日志服务
    @Deprecated
    public static String ots_endPoint() {
        return getInstance().getProperty("ots.endPoint", null);
    }

    @Deprecated
    public static String ots_accessId() {
        return getInstance().getProperty("ots.accessId", null);
    }

    @Deprecated
    public static String ots_accessKey() {
        return getInstance().getProperty("ots.accessKey", null);
    }

    @Deprecated
    public static String ots_instanceName() {
        return getInstance().getProperty("ots.instanceName", null);
    }

    // 简讯服务(旧版本)
    @Deprecated
    public static String sms_host() {
        return getInstance().getProperty("sms.host", null);
    }

    @Deprecated
    public static String sms_username() {
        return getInstance().getProperty("sms.username", null);
    }

    @Deprecated
    public static String sms_password() {
        return getInstance().getProperty("sms.password", null);
    }

    // 微信服务
    public static String wx_host() {
        return getInstance().getProperty("wx.host", null);
    }

    public static String wx_appid() {
        return getInstance().getProperty("wx.appid", null);
    }

    public static String wx_secret() {
        return getInstance().getProperty("wx.secret", null);
    }

    @Deprecated
    public static String dayu_serverUrl() {
        return getInstance().getProperty("dayu.serverUrl", null);
    }

    @Deprecated
    public static String dayu_appKey() {
        return getInstance().getProperty("dayu.appKey", null);
    }

    @Deprecated
    public static String dayu_appSecret() {
        return getInstance().getProperty("dayu.appSecret", null);
    }

    public static String getTaskToken() {
        return getInstance().getProperty(TaskServiceToken, null);
    }

    public static boolean enableTaskService() {
        return "1".equals(getInstance().getProperty(TaskServiceEnabled, null));
    }

    @Deprecated
    public Map<String, String> getDefaultParams() {
        return defaultParams;
    }

    @Deprecated
    public void setDefaultParams(Map<String, String> defaultParams) {
        this.defaultParams = defaultParams;
    }

    @Override
    public String getProperty(String key, String def) {
        String result = null;
        if (properties != null) {
            result = properties.getProperty(key);
            if (result == null) {
                LocalConfig config = LocalConfig.getInstance();
                result = config.getProperty(key, def);
            }
        }
        return result != null ? result : def;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }
}
