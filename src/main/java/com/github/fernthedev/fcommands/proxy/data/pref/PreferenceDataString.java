package com.github.fernthedev.fcommands.proxy.data.pref;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferenceDataString extends PreferenceDataAbstract<String> {
    @Getter
    protected transient List<String> possibleStrings = new ArrayList<>();

    public PreferenceDataString(String name, String value, @Nullable String... possibleValues) {
        super(name, value);
        if (possibleValues != null) {
            possibleStrings.addAll(Arrays.asList(possibleValues));
        }
    }

    @Override
    public List<String> possibleValues() {
        return possibleStrings;
    }

    @Override
    public void isValid(Object value) {
        if (!(value instanceof String)) throw new IllegalArgumentException("Value is not instance of string");
    }

    @Override
    public String parse(String value) throws Exception {
        return value;
    }
}
