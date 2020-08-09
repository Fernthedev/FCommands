package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fcommands.universal.commands.DebugCommand;
import com.github.fernthedev.fcommands.universal.commands.UFernPing;
import com.github.fernthedev.fernapi.universal.Universal;

public class PlatformAllRegistration {

    public static void registerCommands() {
        Universal.getCommandHandler().registerCommand(new UFernPing());
        Universal.getCommandHandler().registerCommand(new DebugCommand());
    }

}
