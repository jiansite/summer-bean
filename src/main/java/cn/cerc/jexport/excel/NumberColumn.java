package cn.cerc.jexport.excel;

import cn.cerc.jdb.other.utils;

public class NumberColumn extends Column {

    public NumberColumn() {
        super();
    }

    public NumberColumn(String code, String name, int width) {
        super(code, name, width);
    }

    @Override
    public Object getValue() {
        return utils.strToFloatDef(utils.formatFloat("0.####", getRecord().getDouble(getCode())), 0);
    }
}
