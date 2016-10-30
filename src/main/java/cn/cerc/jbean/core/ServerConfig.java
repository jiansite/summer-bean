package cn.cerc.jbean.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.cerc.jdb.core.IConfig;

public class ServerConfig implements IConfig {
	private static final Logger log = Logger.getLogger(ServerConfig.class);
	private static Properties properties = new Properties();
	private Map<String, String> defaultParams = new HashMap<>();

	public static final int appNone = 0;
	public static final int appTest = 1;
	public static final int appBeta = 2;
	public static final int appRelease = 3;

	static {
		AppConfig conf = Application.getConfig();
		if (conf == null)
			throw new RuntimeException("config is null");
		String confFile = System.getProperty("user.home") + System.getProperty("file.separator") + conf.getConfigFile();
		try {
			File file2 = new File(confFile);
			if (file2.exists()) {
				properties.load(new FileInputStream(confFile));
				log.info("read properties from : " + confFile);
			} else {
				log.error("not find properties: " + confFile);
			}
		} catch (FileNotFoundException e) {
			log.error("The settings file '" + confFile + "' does not exist.");
		} catch (IOException e) {
			log.error("Failed to load the settings from the file: " + confFile);
		}
	}

	public static String getAppName() {
		String result = getPropertyValue("appName", "localhost");
		return result;
	}

	public static int getAppLevel() {
		String tmp = getPropertyValue("version", "beta");
		if ("test".equals(tmp))
			return 1;
		if ("beta".equals(tmp))
			return 2;
		if ("release".equals(tmp))
			return 3;
		else
			return 0;
	}

	public static int getTimeoutWarn() {
		String str = getPropertyValue("timeout.warn", "60");
		return Integer.parseInt(str); // 默认60秒
	}

	public static String getAdminMobile() {
		return getPropertyValue("admin.mobile", null);
	}

	public static String getAdminEmail() {
		return getPropertyValue("admin.email", null);
	}

	// 日志服务
	public static String ots_endPoint() {
		return getPropertyValue("ots.endPoint", null);
	}

	public static String ots_accessId() {
		return getPropertyValue("ots.accessId", null);
	}

	public static String ots_accessKey() {
		return getPropertyValue("ots.accessKey", null);
	}

	public static String ots_instanceName() {
		return getPropertyValue("ots.instanceName", null);
	}

	// 简讯服务(旧版本)
	public static String sms_host() {
		return getPropertyValue("sms.host", null);
	}

	public static String sms_username() {
		return getPropertyValue("sms.username", null);
	}

	public static String sms_password() {
		return getPropertyValue("sms.password", null);
	}

	// 微信服务
	public static String wx_host() {
		return getPropertyValue("wx.host", null);
	}

	public static String wx_appid() {
		return getPropertyValue("wx.appid", null);
	}

	public static String wx_secret() {
		return getPropertyValue("wx.secret", null);
	}

	public static String dayu_serverUrl() {
		return getPropertyValue("dayu.serverUrl", null);
	}

	public static String dayu_appKey() {
		return getPropertyValue("dayu.appKey", null);
	}

	public static String dayu_appSecret() {
		return getPropertyValue("dayu.appSecret", null);
	}

	@Override
	public String getProperty(String key, String def) {
		String result = null;
		if (properties != null)
			result = properties.getProperty(key);
		return result != null ? result : def;
	}

	private static String getPropertyValue(String key, String def) {
		String result = null;
		if (properties != null)
			result = properties.getProperty(key);
		return result != null ? result : def;
	}

	public static String getTaskToken() {
		return getPropertyValue("task.token", null);
	}

	public static boolean enableTaskService() {
		return "1".equals(getPropertyValue("task.service", null));
	}

	public Map<String, String> getDefaultParams() {
		return defaultParams;
	}

	public void setDefaultParams(Map<String, String> defaultParams) {
		this.defaultParams = defaultParams;
	}
}
