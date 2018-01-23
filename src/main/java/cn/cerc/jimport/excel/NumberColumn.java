package cn.cerc.jimport.excel;

import java.text.DecimalFormat;

import cn.cerc.jdb.other.utils;

public class NumberColumn extends Column {
    @Override
    public Object getValue() {
        return utils.strToFloatDef(new DecimalFormat("0.######").format(getRecord().getDouble(getCode())), 0);
    }

    @Override
    public boolean validate(int row, int col, String value) {
        String result = "";
        if (!"".equals(value)) {
            result = String.valueOf(Math.abs(Double.parseDouble(value)));
        }
        return cn.cerc.jdb.other.utils.isNumeric(result);
    }
}
