package com.github.fernthedev.fcommands.spigotclass.ncp;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;

public class NCPHandle {

    public static void register() {
        NCPHookManager.addHook(CheckType.values(), new bungeencp());
    }

    public static void onDisable() {
        if (NCPHookManager.getHooksByName(FernCommands.getInstance().getName()) != null)
            NCPHookManager.removeHooks(FernCommands.getInstance().getName());
    }
}
