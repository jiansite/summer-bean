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

    @Override
    public void closeConnections() {

    }

    @Override
    public String getUserName() {
        return handle.getUserName();
    }

    @Override
    public void setProperty(String key, Object value) {
        throw new RuntimeException("调用了未被实现的接口");
    }

    @Override
    public boolean init(String bookNo, String userCode, String clientCode) {
        throw new RuntimeException("调用了未被实现的接口");
    }

    @Override
    public boolean init(String token) {
        throw new RuntimeException("调用了未被实现的接口");
    }

    @Override
    public boolean logon() {
        return false;
    }
}
