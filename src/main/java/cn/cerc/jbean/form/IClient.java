package cn.cerc.jbean.form;

public interface IClient {

	public void setForm(IForm form);

	public IForm getForm();
	
	public boolean isPhone();

	// 返回设备Id
	public String getId();

	// 返回设备型号
	public String getDevice();
	
	//返回设备语言
	public String getLanguage();
	
	//设置设备型号
	public void setDevice(String device);
}
