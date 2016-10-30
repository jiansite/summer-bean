package cn.cerc.jbean.core;

import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.mysql.SqlSession;

public class BookHandle implements IHandle {
	private IHandle handle;
	private String corpNo;
	private String userCode;
	private String userName;

	public BookHandle(IHandle handle, String corpNo) {
		this.handle = handle;
		this.corpNo = corpNo;
	}

	public SqlSession getConnection() {
		return (SqlSession) handle.getProperty(SqlSession.sessionId);
	}

	@Override
	public String getCorpNo() {
		return this.corpNo;
	}

	@Override
	public String getUserCode() {
		return userCode != null ? userCode : handle.getUserCode();
	}

	@Override
	public String getUserName() {
		return userName != null ? userName : handle.getUserName();
	}

	@Override
	public Object getProperty(String key) {
		return handle.getProperty(key);
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
