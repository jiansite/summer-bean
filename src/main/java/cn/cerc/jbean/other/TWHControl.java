package cn.cerc.jbean.other;

import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.Record;

public enum TWHControl {
	whcNone, whcHead, whcBody;

	/*
	 * 取得单头仓别
	 */
	public String getHeadWH(Record head) {
		return this == TWHControl.whcNone ? "仓库" : head.getSafeString("WHCode_");
	}

	/*
	 * 取得单身仓别
	 */
	public String getBodyWH(DataSet body, String defaultWH) {
		if (this == whcBody)
			return body.getCurrent().getSafeString("CWCode_");
		else
			return defaultWH;
	}
}