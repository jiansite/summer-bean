package cn.cerc.jbean.rds;

import cn.cerc.jbean.core.Application;
import cn.cerc.jbean.core.ServerConfig;
import cn.cerc.jbean.other.SystemTable;
import cn.cerc.jdb.cache.CacheConnection;
import cn.cerc.jdb.cache.CacheSession;
import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.jiguang.JiguangConnection;
import cn.cerc.jdb.jiguang.JiguangSession;
import cn.cerc.jdb.mysql.SqlConnection;
import cn.cerc.jdb.mysql.SqlQuery;
import cn.cerc.jdb.mysql.SqlSession;
import cn.cerc.jdb.queue.QueueConnection;
import cn.cerc.jdb.queue.QueueSession;

public class StubHandle implements IHandle, AutoCloseable {
	public static final String DefaultBook = "999001";
	public static final String DefaultUser = DefaultBook + "01";
	public static final String DefaultProduct = "999001000001";
	// 生产部
	public static final String DefaultDept = "10050001";
	private IHandle handle;
	private static final String clientIP = "127.0.0.1";

	public StubHandle() {
		this(DefaultBook, DefaultUser);
	}

	public StubHandle(String corpNo) {
		handle = Application.getHandle();

		String userCode;
		SqlQuery ds = new SqlQuery(this);

		ds.setMaximum(1);
		ds.add("select Code_ from " + SystemTable.get(SystemTable.getUserInfo));
		ds.add("where CorpNo_='%s'", corpNo);
		ds.open();
		if (ds.eof())
			throw new RuntimeException("找不到默认帐号：CorpNo=" + corpNo);
		userCode = ds.getString("Code_");

		handle.init(corpNo, userCode, clientIP);
	}

	public StubHandle(String corpNo, String userCode) {
		handle = Application.getHandle();
		handle.init(corpNo, userCode, clientIP);
	}

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
	public String getUserName() {
		return handle.getUserName();
	}

	@Override
	public Object getProperty(String key) {
		if ("request".equals(key))
			return null;
		Object obj = handle.getProperty(key);
		if (obj == null && SqlSession.sessionId.equals(key)) {
			SqlConnection conn = new SqlConnection();
			conn.setConfig(new ServerConfig());
			obj = conn.getSession();
			handle.setProperty(key, obj);
		}
		if (obj == null && CacheSession.sessionId.equals(key)) {
			CacheConnection conn = new CacheConnection();
			conn.setConfig(new ServerConfig());
			obj = conn.getSession();
			handle.setProperty(key, obj);
		}
		if (obj == null && QueueSession.sessionId.equals(key)) {
			QueueConnection conn = new QueueConnection();
			conn.setConfig(new ServerConfig());
			obj = conn.getSession();
			handle.setProperty(key, obj);
		}
		if (obj == null && JiguangSession.sessionId.equals(key)) {
			JiguangConnection conn = new JiguangConnection();
			conn.setConfig(new ServerConfig());
			obj = conn.getSession();
			handle.setProperty(key, obj);
		}
		return obj;
	}

	@Override
	public void close() {
		handle.closeConnections();
	}

}
