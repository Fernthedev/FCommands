package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fcommands.universal.commands.DebugCommand;
import com.github.fernthedev.fcommands.universal.commands.UFernPing;
import com.github.fernthedev.fernapi.universal.Universal;

import java.lang.reflect.InvocationTargetException;

public class PlatformAllRegistration {

    public static void commonInit() {
        Universal.getCommandHandler().enableUnstableAPI("help");
        Universal.getCommandHandler().registerCommand(new UFernPing());
        Universal.getCommandHandler().registerCommand(new DebugCommand());

        if (Universal.getMethods().getServerType().isProxy()) {

            try {
                // Use reflection to avoid classpath issues with Spigot
                Class<?> aClass = Class.forName("com.github.fernthedev.preferences.core.PreferenceManager");

                Class<?> preferenceClass = Class.forName("com.github.fernthedev.preferences.core.PluginPreference");

                aClass.getMethod("registerPreference", preferenceClass).invoke(null, new PluginPreferenceManager());
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }

}
