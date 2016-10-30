package cn.cerc.jbean.form;

public interface IJspPage extends IPage {
	// 输出到支持Jstl的jsp文件
	public void add(String id, Object value);

	public String getMessage();

	public void setMessage(String message);

	public void setFile(String jspFile);

	// 返回带设备码的jsp文件
	public String getViewFile();

	// 此函数为兼容老的写法，后续不再使用!
	default public IJspPage getPage() {
		return this;
	}
}
