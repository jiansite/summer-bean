package cn.cerc.jbean.services;

import cn.cerc.jbean.core.AbstractService;
import cn.cerc.jbean.core.IStatus;
import cn.cerc.jbean.core.ServiceException;
import cn.cerc.jbean.other.SystemTable;
import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.SqlQuery;

public class SvrUserOption extends AbstractService {

	@Override
	public IStatus execute(DataSet dataIn, DataSet dataOut) throws ServiceException {
		Record head = dataIn.getHead();
		SqlQuery ds = new SqlQuery(this);
		ds.add(String.format("select Value_ from %s", SystemTable.get(SystemTable.getUserOptions)));
		ds.add(String.format("where UserCode_=N'%s' and Code_=N'%s'", this.getUserCode(), head.getSafeString("Code_")));
		ds.open();
		dataOut.appendDataSet(ds);
		return success();
	}
}