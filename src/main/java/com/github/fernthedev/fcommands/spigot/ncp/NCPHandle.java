package com.github.fernthedev.fcommands.spigot.ncp;

import com.google.inject.Injector;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;

public class NCPHandle {

    public static void register(Injector injector) {
        NCPHookManager.addHook(CheckType.values(), injector.getInstance(BungeeNCP.class));
    }

    public static void onDisable() {

    }
}
