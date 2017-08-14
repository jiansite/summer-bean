package cn.cerc.jbean.client;

import cn.cerc.jbean.core.Application;
import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.IStatus;
import cn.cerc.jbean.core.ServiceStatus;
import cn.cerc.jdb.core.DataSet;

public class Microservice extends CustomService {
    // 代理位置
    private String location;
    private String service;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public IStatus execute(DataSet dataIn, DataSet dataOut) {
        if (dataIn != null)
            this.dataIn = dataIn;
        if (dataOut != null)
            this.dataOut = dataOut;

        String host = Application.getServerConfig().getProperty("microservice." + location, "127.0.0.1");
        String token = (String) this.getHandle().getProperty(Application.token);

        RemoteService app = new RemoteService();
        app.setService(this.service);
        app.setHost(host);
        app.setToken(token);
        app.setDataIn(this.getDataIn());
        app.setDataOut(this.getDataOut());
        ServiceStatus status = new ServiceStatus(true);
        try {
            boolean rst = app.exec();
            status.setMessage(app.getMessage());
            status.setResult(rst);
        } catch (Exception e) {
            status.setMessage(e.getMessage());
            status.setResult(true);
        }
        return status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
