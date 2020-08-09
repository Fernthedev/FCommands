package com.github.fernthedev.fcommands.proxy.data.pref;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class PreferenceDataTimezone extends PreferenceDataString {
    private static final List<String> timeZones = Arrays.asList(TimeZone.getAvailableIDs());

    public PreferenceDataTimezone(String name, String value) {
        super(name, value);
        possibleStrings = timeZones;
    }

    @Override
    public List<String> possibleValues() {
        return timeZones;
    }

//    public static List<String> possibleValuesS() {
//        return Arrays.asList(TimeZone.getAvailableIDs());
//    }

    @Override
    public void isValid(Object value) {
        super.isValid(value);
        if (TimeZone.getTimeZone((String) value) == null) invalidFormat((String) value);
    }

    @Override
    public String parse(String value) throws Exception {
        if (TimeZone.getTimeZone(value) == null) invalidFormat(value);

        return super.parse(value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PreferenceDataTimezone{");
        sb.append("possibleStrings=").append(possibleStrings);
        sb.append(", name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
