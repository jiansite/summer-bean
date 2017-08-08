package cn.cerc.jbean.form;

public interface IMenu {

    // 菜单代码，建议以 Frm 开头
    String getId();

    // 菜单标题
    String getTitle();

    // 菜单编号，一般为纯数字
    String getPageNo();

    // true: 需要登录方可使用
    boolean isSecurityEnabled();

    // 菜单授权码
    String getPermissionCode();

    // 软件类别，如 1,2,，其中1及2各代表一种软件
    String getSoftwareList();

    // 上级菜单，若无，则为""
    String getParentId();

    // 菜单图标，为URL值
    String getImage();

    // 设置参数
    void setParam(String key, String value);

    // 取得参数
    String getParam(String key);
}
