package cn.cerc.jbean.core;

import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.IHandle;

public interface IService {
    public void init(IHandle handle);

    public IStatus execute(DataSet dataIn, DataSet dataOut) throws ServiceException;

    default ServiceStatus fail(String format, Object... args) {
        ServiceStatus status = new ServiceStatus(false);
        if (args.length > 0)
            status.setMessage(String.format(format, args));
        else
            status.setMessage(format);
        return status;
    }

    public default ServiceStatus success() {
        return new ServiceStatus(true);
    }

    public default ServiceStatus success(String format, Object... args) {
        ServiceStatus status = new ServiceStatus(true);
        if (args.length > 0)
            status.setMessage(String.format(format, args));
        else
            status.setMessage(format);
        return status;
    }

    default public boolean checkSecurity(IHandle handle) {
        IHandle sess = (IHandle) handle.getProperty(null);
        return sess != null ? sess.logon() : false;
    }

    // 主要适用于Delphi Client调用
    default public String getJSON(DataSet dataOut) {
        return String.format("[%s]", dataOut.getJSON());
    }
}
