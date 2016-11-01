package cn.cerc.jbean.form;

public interface IJspPage extends IPage {
	// 输出到支持Jstl的jsp文件
	public void add(String id, Object value);

	public String getMessage();

	public void setMessage(String message);

	public void setFile(String jspFile);

	// 返回带设备码的jsp文件
	public String getViewFile();
}
