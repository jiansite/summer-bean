package cn.cerc.jbean.other;

import cn.cerc.jbean.core.Application;

public class SystemTable {
	// 帐套资料表
	public static final String getBookInfo = "OurInfo";
	// 帐套参数档
	public static final String getBookOptions = "VineOptions";
	// 应用菜单表
	public static final String getAppMenus = "SysFormDef";
	// 客户客制化菜单
	public static final String getCustomMenus = "cusmenu";
	// 用户自定义菜单
	public static final String getUserMenus = "UserMenu";

	// 用户资料表
	public static final String getUserInfo = "Account";
	// 用户参数表
	public static final String getUserOptions = "UserOptions";
	// 用户角色表
	public static final String getUserRoles = "UserRoles";
	// 角色权限表
	public static final String getRoleAccess = "UserAccess";
	// 用户设备认证记录表
	public static final String getDeviceVerify = "AccountVerify";

	// 当前在线用户
	public static final String getCurrentUser = "CurrentUser";
	// 记录用户需要查看的消息
	public static final String getUserMessages = "message_temp";
	// 记录用户的关键操作
	public static final String getUserLogs = "UserLogs";
	// 记录应用服务被调用的历史
	public static final String getAppLogs = "AppServiceLogs";
	// 记录网页被调用的历史
	public static final String getPageLogs = "WebPageLogs";

	// 运营商帐套代码
	public static final String ManageBook = "000000";

	public static String get(String tableCode) {
		return Application.getAppConfig().getParam(tableCode, tableCode);
	}

	// 表格列自定义存储表，建议存于MongoDB
	public static String getGridManager() {
		return "s_gridManager";
	}
}