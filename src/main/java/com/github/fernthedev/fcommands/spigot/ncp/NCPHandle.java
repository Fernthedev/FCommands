package com.github.fernthedev.fcommands.spigot.ncp;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;

public class NCPHandle {

    public static void register() {
        NCPHookManager.addHook(CheckType.values(), new BungeeNCP());
    }

    public static void onDisable() {
        if (NCPHookManager.getHooksByName(FernCommands.getInstance().getName()) != null)
            NCPHookManager.removeHooks(FernCommands.getInstance().getName());
    }
}
