package com.github.fernthedev.fcommands.proxy.data.pref;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class PreferenceDataNumber extends PreferenceDataAbstract<Number> {
    private List<Number> integers;

    public PreferenceDataNumber(String name, Number value, Number... integers) {
        super(name, value);
        this.integers = Arrays.asList(integers);
    }

    @Override
    public List<Number> possibleValues() {
        return integers;
    }

    @Override
    public void isValid(Object value) throws ParseException {
        if (value instanceof String) NumberFormat.getInstance().parse((String) value);
        else if (!(value instanceof Number)) throw new IllegalArgumentException("Value is not instance of boolean");
    }

    @Override
    public Number parse(String value) throws ParseException {
        return NumberFormat.getInstance().parse(value);
    }


}
