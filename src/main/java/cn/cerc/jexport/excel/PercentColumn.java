package cn.cerc.jexport.excel;

import cn.cerc.jdb.other.utils;

public class PercentColumn extends Column {

    @Override
    public Object getValue() {
        String value = utils.formatFloat("0.##", getRecord().getDouble(getCode()));
        return String.format("%s%%", value);
    }
}
