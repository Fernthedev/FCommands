package com.github.fernthedev.fcommands.proxy.data.pref;

import java.util.ArrayList;
import java.util.List;

public class PreferenceDataBoolean extends PreferenceDataAbstract<Boolean> {
    public PreferenceDataBoolean(String name, Boolean value) {
        super(name, value);
    }

    @Override
    public List<Boolean> possibleValues() {
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(true);
        booleanList.add(false);
        return booleanList;
    }

    @Override
    public void isValid(Object value) {
        if (value instanceof String) parseBoolean((String) value);
        else if (!(value instanceof Boolean)) throw new IllegalArgumentException("Value is not instance of boolean");
    }

    @Override
    public Boolean parse(String value) {
        return parseBoolean(value);
    }

    private boolean parseBoolean(String s) {
        switch (s.toLowerCase()) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                invalidFormat(s);
                return false;
        }
    }
}
