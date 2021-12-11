package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.preferences.api.PluginPreference;
import com.github.fernthedev.preferences.api.data.PreferenceDataBoolean;
import com.github.fernthedev.preferences.api.data.PreferenceDataTimezone;

public class PluginPreferenceManager extends PluginPreference {
    public static final PreferenceDataTimezone PREFERRED_TIMEZONE = new PreferenceDataTimezone("zone","UTC");
    public static final PreferenceDataBoolean HOUR_12_FORMAT = new PreferenceDataBoolean("hour12", false);

    public static final String NAMESPACE = "ferncommands";

    public PluginPreferenceManager() {
        super(NAMESPACE);
        register(PREFERRED_TIMEZONE);
        register(HOUR_12_FORMAT);
    }
}
