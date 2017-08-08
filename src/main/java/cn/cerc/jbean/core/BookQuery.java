package cn.cerc.jbean.core;

import cn.cerc.jdb.core.IDataOperator;
import cn.cerc.jdb.core.IHandle;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.SqlOperator;
import cn.cerc.jdb.mysql.SqlQuery;

public class BookQuery extends SqlQuery implements IDataOperator {
    private static final long serialVersionUID = 7447239243975915295L;
    private IDataOperator operator;
    private IHandle handle;

    public BookQuery(IHandle handle) {
        super(handle);
        this.handle = handle;
    }

    @Override
    public IDataOperator getDefaultOperator() {
        if (operator == null) {
            SqlOperator def = new SqlOperator(handle);
            String tableName = SqlOperator.findTableName(this.getCommandText());
            def.setTableName(tableName);
            operator = def;
        }
        this.setOperator(this);
        return this;
    }

    @Override
    public boolean insert(Record record) {
        String corpNo = record.getString("CorpNo_");
        if (!handle.getCorpNo().equals(corpNo))
            throw new RuntimeException(String.format("corpNo: %s, insert error value: %s", handle.getCorpNo(), corpNo));
        return operator.insert(record);
    }

    @Override
    public boolean update(Record record) {
        String corpNo = record.getString("CorpNo_");
        if (!handle.getCorpNo().equals(corpNo))
            throw new RuntimeException(String.format("corpNo: %s, update error value: %s", handle.getCorpNo(), corpNo));
        return operator.update(record);
    }

    @Override
    public boolean delete(Record record) {
        String corpNo = record.getString("CorpNo_");
        if (!handle.getCorpNo().equals(corpNo))
            throw new RuntimeException(String.format("corpNo: %s, delete error value: %s", handle.getCorpNo(), corpNo));
        return operator.delete(record);
    }
}
