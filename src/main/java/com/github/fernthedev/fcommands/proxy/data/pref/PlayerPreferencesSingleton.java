package com.github.fernthedev.fcommands.proxy.data.pref;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerPreferencesSingleton {

    private PreferenceDataTimezone preferredTimezone = new PreferenceDataTimezone("zone","UTC");
    private PreferenceDataBoolean hour12Format = new PreferenceDataBoolean("hour12", false);


    public PreferenceDataAbstract<?> getFromString(String name) throws NoSuchFieldException, IllegalAccessException {
        return (PreferenceDataAbstract<?>) getClass().getDeclaredField(name).get(this);
    }

    public List<Field> getPossibleFields() {
        return Arrays.stream(getClass().getDeclaredFields()).filter(field -> PreferenceDataAbstract.class.isAssignableFrom(field.getType())).collect(Collectors.toList());
    }

    public PreferenceDataTimezone getPreferredTimezone() {
        return this.preferredTimezone;
    }

    public PreferenceDataBoolean getHour12Format() {
        return this.hour12Format;
    }

    public String toString() {
        return "PlayerPreferencesSingleton(preferredTimezone=" + this.getPreferredTimezone() + ", hour12Format=" + this.getHour12Format() + ")";
    }
}
