package cn.cerc.jbean.book;

import cn.cerc.jbean.tools.DataUpdateException;

public interface IResetBook extends IBook {
	// 对登记到帐本的的数据进行重置（回算）
	public void reset() throws DataUpdateException;
}
