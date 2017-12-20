package cn.cerc.jbean.core;

import cn.cerc.jbean.form.IForm;
import cn.cerc.jbean.rds.PassportRecord;

public interface IPassport {

    // 是否有菜单的执行权限
    default boolean passForm(IForm form) {
        String securityCheck = form.getParam("security", "true");
        if (!"true".equals(securityCheck)) {
            return true;
        }
        String verList = form.getParam("verlist", null);
        String procCode = form.getPermission();
        return passProc(verList, procCode);
    }

    // 是否有程序的执行权限
    boolean passProc(String versions, String procCode);

    // 是否有程序指定动作的权限
    public boolean passAction(String procCode, String action);

    // 返回指定程序的权限记录
    public PassportRecord getRecord(String procCode);

    // 是否有菜单的执行权限
    public boolean passsMenu(String menuCode);
}
