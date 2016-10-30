package cn.cerc.jbean.form;

import javax.servlet.http.HttpServletRequest;

public interface IClient {

	public void setRequest(HttpServletRequest request);

	public HttpServletRequest getRequest();

	public boolean isPhone();

	// 返回设备Id
	public String getId();

	// 返回设备型号
	public String getDevice();
}
