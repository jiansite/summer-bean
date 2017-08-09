package cn.cerc.jbean.core;

import org.junit.Ignore;
import org.junit.Test;

import cn.cerc.jbean.other.SystemTable;
import cn.cerc.jbean.rds.StubHandle;
import cn.cerc.jdb.core.TDateTime;

public class BookQueryTest {
    StubHandle handle = new StubHandle();
    BookQuery ds = new BookQuery(handle);

    @Test(expected = RuntimeException.class)
    @Ignore
    public void test() {
        ds.add("select * from %s where CorpNo_='144001'", SystemTable.get(SystemTable.getBookInfo));
        ds.open();
        ds.edit();
        ds.setField("UpdateKey_", TDateTime.Now());
        ds.post();
    }

}
