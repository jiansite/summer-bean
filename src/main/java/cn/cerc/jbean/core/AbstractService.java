package cn.cerc.jbean.core;

import cn.cerc.jdb.core.IHandle;

public abstract class AbstractService extends AbstractHandle implements IService, IRestful {
    private String restPath;

    @Override
    public void setRestPath(String restPath) {
        this.restPath = restPath;
    }

    @Override
    public String getRestPath() {
        return restPath;
    }

    @Override
    public void init(IHandle handle) {
        this.handle = handle;
    }
}
