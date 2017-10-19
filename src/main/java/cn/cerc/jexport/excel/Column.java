package cn.cerc.jexport.excel;

import cn.cerc.jdb.core.Record;

public abstract class Column {
    // 对应数据集字段名
    private String code;
    // 对应数据集字段标题
    private String name;
    // 列宽度
    private int width;
    // 数据源
    private Record record;

    // 取得数据
    public abstract Object getValue();

    public String getString() {
        return record.getString(code);
    }

    public String getCode() {
        return code;
    }

    public Column setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Column setName(String name) {
        this.name = name;
        return this;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public int getWidth() {
        return width;
    }

    public Column setWidth(int width) {
        this.width = width;
        return this;
    }
}