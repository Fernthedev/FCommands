package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.preferences.api.PluginPreference;
import com.github.fernthedev.preferences.api.data.PreferenceDataBoolean;
import com.github.fernthedev.preferences.api.data.PreferenceDataTimezone;

public class PluginPreferenceManager extends PluginPreference {
    public static final PreferenceDataTimezone preferredTimezone = new PreferenceDataTimezone("zone","UTC");
    public static final PreferenceDataBoolean hour12Format = new PreferenceDataBoolean("hour12", false);

    public static final String NAMESPACE = "ferncommands";

    public PluginPreferenceManager() {
        super(NAMESPACE);
        register(preferredTimezone);
        register(hour12Format);
    }
}
