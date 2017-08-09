package cn.cerc.jbean.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.cerc.jbean.client.LocalService;
import cn.cerc.jbean.other.BufferType;
import cn.cerc.jbean.other.MemoryBuffer;
import cn.cerc.jbean.other.UserNotFindException;
import cn.cerc.jdb.core.IConnection;
import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.core.ISession;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.SqlSession;

public class CustomHandle implements IHandle, AutoCloseable {
    private static final Logger log = Logger.getLogger(CustomHandle.class);
    private Map<String, IConnection> connections = new HashMap<>();
    private Map<String, Object> params = new HashMap<>();

    public CustomHandle() {
        params.put(Application.sessionId, "");
        params.put(Application.ProxyUsers, "");
        params.put(Application.clientIP, "0.0.0.0");
        params.put(Application.userCode, "");
        params.put(Application.userName, "");
        params.put(Application.roleCode, "");
        params.put(Application.bookNo, "");
        params.put(Application.deviceLanguage, Application.LangageDefault);
    }

    @Override
    public boolean init(String corpNo, String userCode, String clientCode) {
        String token = GuidFixStr(cn.cerc.jdb.other.utils.newGuid());
        this.setProperty(Application.token, token);
        this.setProperty(Application.bookNo, corpNo);
        this.setProperty(Application.userCode, userCode);
        this.setProperty(Application.clientIP, clientCode);

        LocalService svr = new LocalService(this, "AppSessionRestore.byUserCode");
        if (!svr.exec("userCode", userCode)) {
            throw new RuntimeException(new UserNotFindException(userCode));
        }
        Record headOut = svr.getDataOut().getHead();
        this.setProperty(Application.userId, headOut.getString("UserID_"));
        this.setProperty(Application.loginTime, headOut.getDateTime("LoginTime_"));
        this.setProperty(Application.roleCode, headOut.getString("RoleCode_"));
        this.setProperty(Application.ProxyUsers, headOut.getString("ProxyUsers_"));
        this.setProperty(Application.userName, headOut.getString("UserName_"));
        this.setProperty(Application.deviceLanguage, headOut.getString("Language_"));

        try (MemoryBuffer buff = new MemoryBuffer(BufferType.getSessionBase, token)) {
            buff.setField("LoginTime_", headOut.getDateTime("LoginTime_"));
            buff.setField("UserID_", headOut.getString("UserID_"));
            buff.setField("UserCode_", userCode);
            buff.setField("CorpNo_", corpNo);
            buff.setField("UserName_", headOut.getString("UserName_"));
            buff.setField("RoleCode_", headOut.getString("RoleCode_"));
            buff.setField("ProxyUsers_", headOut.getString("ProxyUsers_"));
            buff.setField("Language_", headOut.getString("Language_"));
            buff.setField("exists", true);
        }
        return true;
    }

    @Override
    public boolean init(String token) {
        this.setProperty(Application.token, token);
        log.debug(String.format("根据 token=%s 初始化 Session", token));
        if (token == null)
            return false;
        if (token.length() < 10)
            throw new RuntimeException("token 值有错！");

        // 从数据表CurrentUser中，取出公司别CorpNo_与UserCode_，再依据UserCode_从Account取出RoleCode_
        try (MemoryBuffer buff = new MemoryBuffer(BufferType.getSessionBase, token)) {
            if (buff.isNull()) {
                buff.setField("exists", false);
                LocalService svr = new LocalService(this, "AppSessionRestore.byToken");
                if (!svr.exec("token", token)) {
                    this.setProperty(Application.token, null);
                    return false;
                }
                Record headOut = svr.getDataOut().getHead();
                buff.setField("LoginTime_", headOut.getDateTime("LoginTime_"));
                buff.setField("UserID_", headOut.getString("UserID_"));
                buff.setField("UserCode_", headOut.getString("UserCode_"));
                buff.setField("CorpNo_", headOut.getString("CorpNo_"));
                buff.setField("UserName_", headOut.getString("UserName_"));
                buff.setField("RoleCode_", headOut.getString("RoleCode_"));
                buff.setField("ProxyUsers_", headOut.getString("ProxyUsers_"));
                buff.setField("Language_", headOut.getString("Language_"));
                buff.setField("exists", true);
            }
            if (buff.getBoolean("exists")) {
                this.setProperty(Application.loginTime, buff.getDateTime("LoginTime_"));
                this.setProperty(Application.bookNo, buff.getString("CorpNo_"));
                this.setProperty(Application.userId, buff.getString("UserID_"));
                this.setProperty(Application.userCode, buff.getString("UserCode_"));
                this.setProperty(Application.userName, buff.getString("UserName_"));
                this.setProperty(Application.ProxyUsers, buff.getString("ProxyUsers_"));
                this.setProperty(Application.roleCode, buff.getString("RoleCode_"));
                this.setProperty(Application.deviceLanguage, buff.getString("Language_"));
                return true;
            } else {
                return false;
            }
        }
    }

    private String GuidFixStr(String guid) {
        String str = guid.substring(1, guid.length() - 1);
        return str.replaceAll("-", "");
    }

    @Override
    public String getCorpNo() {
        return (String) this.getProperty(Application.bookNo);
    }

    @Override
    public boolean logon() {
        if (this.getProperty(Application.token) == null)
            return false;
        String corpNo = this.getCorpNo();
        if (corpNo == null || "".equals(corpNo))
            return false;
        return true;
    }

    @Override
    public Object getProperty(String key) {
        if (key == null)
            return this;
        Object result = params.get(key);
        if (result == null && !params.containsKey(key) && connections.containsKey(key)) {
            IConnection conn = connections.get(key);
            result = conn.getSession();
            params.put(key, result);
        }
        return result;
    }

    @Override
    public void setProperty(String key, Object value) {
        if (Application.token.equals(key)) {
            if ("{}".equals(value) || "".equals(key))
                params.put(key, null);
            else
                params.put(key, value);
            return;
        }
        params.put(key, value);
    }

    @Override
    public String getUserName() {
        return (String) this.getProperty(Application.userName);
    }

    @Override
    public String getUserCode() {
        return (String) this.getProperty(Application.userCode);
    }

    @Override
    public void closeConnections() {
        for (String key : this.params.keySet()) {
            Object sess = this.params.get(key);
            try {
                if (sess instanceof ISession)
                    ((ISession) sess).closeSession();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        this.closeConnections();
    }

    public SqlSession getConnection() {
        return (SqlSession) getProperty(SqlSession.sessionId);
    }

    public Map<String, IConnection> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, IConnection> connections) {
        this.connections = connections;
    }
}
