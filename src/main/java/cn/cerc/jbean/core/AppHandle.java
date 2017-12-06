package cn.cerc.jbean.core;

import cn.cerc.jdb.mysql.SqlSession;

public class AppHandle extends AbstractHandle implements AutoCloseable {
    public AppHandle() {
        handle = Application.getHandle();
    }

    @Override
    public void close() {
        this.closeConnections();
    }

    @Override
    public SqlSession getConnection() {
        return (SqlSession) handle.getProperty(SqlSession.sessionId);
    }

    @Override
    public void closeConnections() {
        this.getConnection().closeSession();
    }

    @Override
    public String getUserName() {
        return handle.getUserName();
    }

    @Override
    public void setProperty(String key, Object value) {
        handle.setProperty(key, value);
    }

    @Override
    public boolean init(String bookNo, String userCode, String clientCode) {
        return handle.init(bookNo, userCode, clientCode);
    }

    @Override
    public boolean init(String token) {
        return handle.init(token);
    }

    @Override
    public boolean logon() {
        return handle.logon();
    }
}
