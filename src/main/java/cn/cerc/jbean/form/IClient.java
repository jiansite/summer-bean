package cn.cerc.jbean.form;

public interface IClient {

	public void setForm(IForm form);

	public IForm getForm();
	
	public boolean isPhone();

	// 返回设备Id
	public String getId();

	// 返回设备型号
	public String getDevice();
}
