package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.preferences.core.PluginPreference;
import com.github.fernthedev.preferences.core.data.PreferenceDataBoolean;
import com.github.fernthedev.preferences.core.data.PreferenceDataTimezone;

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
