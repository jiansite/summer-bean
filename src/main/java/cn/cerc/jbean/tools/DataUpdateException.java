package cn.cerc.jbean.tools;

import cn.cerc.jbean.core.ServiceException;

public class DataUpdateException extends ServiceException {
    private static final long serialVersionUID = -8184184817999373005L;

    public DataUpdateException(Exception e) {
        super(e.getMessage());
        this.addSuppressed(e);
    }

    public DataUpdateException(String message) {
        super(message);
    }
}
