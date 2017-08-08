package cn.cerc.jbean.core;

import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.mysql.SqlSession;

public class AbstractHandle implements IHandle {
    protected IHandle handle;

    public SqlSession getConnection() {
        return (SqlSession) handle.getProperty(SqlSession.sessionId);
    }

    @Override
    public String getCorpNo() {
        return handle.getCorpNo();
    }

    @Override
    public String getUserCode() {
        return handle.getUserCode();
    }

    @Override
    public Object getProperty(String key) {
        return handle.getProperty(key);
    }

    public IHandle getHandle() {
        return handle;
    }

    public void setHandle(IHandle handle) {
        this.handle = handle;
    }
}
