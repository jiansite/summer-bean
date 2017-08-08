package cn.cerc.jbean.services;

import org.apache.log4j.Logger;

import cn.cerc.jbean.core.Application;
import cn.cerc.jbean.core.CustomHandle;
import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.ServiceException;
import cn.cerc.jbean.other.SystemTable;
import cn.cerc.jbean.other.UserNotFindException;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.core.TDateTime;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppSessionRestore extends CustomService {
    private static final Logger log = Logger.getLogger(AppSessionRestore.class);

    public boolean byUserCode() throws ServiceException, UserNotFindException {
        String userCode = getDataIn().getHead().getString("userCode");

        SqlQuery ds = new SqlQuery(this);
        ds.add("select ID_,Code_,RoleCode_,DiyRole_,CorpNo_, Name_ as UserName_,ProxyUsers_ from %s where Code_= '%s' ",
                SystemTable.get(SystemTable.getUserInfo), userCode);
        ds.open();
        if (ds.eof())
            throw new UserNotFindException(userCode);

        Record headOut = getDataOut().getHead();
        headOut.setField("LoginTime_", TDateTime.Now());
        copyData(ds, headOut);
        return true;
    }

    public boolean byToken() throws ServiceException {
        String token = getDataIn().getHead().getString("token");
        SqlQuery ds1 = new SqlQuery(this);
        SqlQuery ds = new SqlQuery(this);
        ds1.add("select CorpNo_,UserID_,LoginTime_,Account_ as UserCode_,Language_ from %s where loginID_= '%s' ",
                SystemTable.get(SystemTable.getCurrentUser), token);
        ds1.open();
        if (ds1.eof()) {
            log.warn(String.format("token %s 没有找到！", token));
            CustomHandle sess = (CustomHandle) this.getProperty(null);
            sess.setProperty(Application.token, null);
            return false;
        }

        String userId = ds1.getString("UserID_");
        ds.add("select ID_,Code_,DiyRole_,RoleCode_,CorpNo_, Name_ as UserName_,ProxyUsers_ from %s where ID_= '%s' ",
                SystemTable.get(SystemTable.getUserInfo), userId);
        ds.open();
        if (ds.eof()) {
            log.warn(String.format("userId %s 没有找到！", userId));
            CustomHandle sess = (CustomHandle) this.getProperty(null);
            sess.setProperty(Application.token, null);
            return false;
        }

        Record headOut = getDataOut().getHead();
        headOut.setField("LoginTime_", ds1.getDateTime("LoginTime_"));
        headOut.setField("Language_", ds1.getString("Language_"));
        copyData(ds, headOut);
        return true;
    }

    private void copyData(SqlQuery ds, Record headOut) {
        headOut.setField("UserID_", ds.getString("ID_"));
        headOut.setField("UserCode_", ds.getString("Code_"));
        headOut.setField("UserName_", ds.getString("UserName_"));
        headOut.setField("CorpNo_", ds.getString("CorpNo_"));
        if (ds.getBoolean("DiyRole_"))
            headOut.setField("RoleCode_", ds.getString("Code_"));
        else
            headOut.setField("RoleCode_", ds.getString("RoleCode_"));
        headOut.setField("ProxyUsers_", ds.getString("ProxyUsers_"));
    }

}
