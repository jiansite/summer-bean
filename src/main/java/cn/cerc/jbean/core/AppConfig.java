package cn.cerc.jbean.core;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
	private Map<String, String> params = new HashMap<>();

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	// 本地配置文件，主要是配置外围连接参数，如数据库等
	public String getConfigFile() {
		return getParam("configFile", "vine.properties");
	}

	public String getPathForms() {
		return getParam("pathForms", "forms");
	}

	public String getPathServices() {
		return getParam("pathServices", "services");
	}

	/**
	 * 
	 * @return 返回默认的欢迎页
	 */
	public String getFormWelcome() {
		return getParam("formWelcome", "Welcome");
	}

	/**
	 * 
	 * @return 返回默认的主菜单
	 */
	public String getFormDefault() {
		return getParam("formDefault", "Default");
	}

	/**
	 *
	 * @return 退出系统确认画面
	 */
	public String getFormLogout() {
		return getParam("formLogout", "Logout");
	}

	/**
	 * 
	 * @return 当前设备第一次登录时需要验证设备
	 */
	public String getFormVerifyDevice() {
		return getParam("formVerifyDevice", "VerifyDevice");
	}

	/**
	 * 
	 * @return 出错时要显示的 jsp 文件
	 */
	public String getJspErrorFile() {
		return getParam("jspErrorFile", "/WEB-INF/forms/common/error.jsp");
	}

	/**
	 * 
	 * @return 在需要用户输入帐号、密码进行登录时的显示
	 */
	public String getJspLoginFile() {
		return getParam("jspLoginFile", "/WEB-INF/forms/common/login.jsp");
	}

	public String getParam(String key, String def) {
		String val = params.get(key);
		return val != null ? val : def;
	}
}
