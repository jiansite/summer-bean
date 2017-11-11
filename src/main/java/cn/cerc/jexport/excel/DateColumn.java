package cn.cerc.jexport.excel;

public class DateColumn extends Column {

    public DateColumn() {
        super();
    }

    public DateColumn(String code, String name, int width) {
        super(code, name, width);
    }

    @Override
    public Object getValue() {
        return getRecord().getDate(getCode());
    }
}
