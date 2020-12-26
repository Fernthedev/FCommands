package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fcommands.universal.commands.DebugCommand;
import com.github.fernthedev.fcommands.universal.commands.UFernPing;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.preferences.core.PreferenceManager;

public class PlatformAllRegistration {

    public static void commonInit() {
        Universal.getCommandHandler().enableUnstableAPI("help");
        Universal.getCommandHandler().registerCommand(new UFernPing());
        Universal.getCommandHandler().registerCommand(new DebugCommand());

        if (Universal.getMethods().getServerType().isProxy()) {
            PreferenceManager.registerPreference(new PluginPreferenceManager());
        }


    }

}
