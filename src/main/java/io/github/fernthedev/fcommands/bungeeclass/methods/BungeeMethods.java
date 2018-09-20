package io.github.fernthedev.fcommands.bungeeclass.methods;

import io.github.fernthedev.fcommands.Universal.MethodInterface;
import io.github.fernthedev.fcommands.Universal.ServerType;
import io.github.fernthedev.fcommands.bungeeclass.FernCommands;

import java.util.logging.Logger;

public class BungeeMethods implements MethodInterface {
    @Override
    public Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }


    @Override
    public Object getInstance() {
        return FernCommands.getInstance();
    }

    @Override
    public ServerType getServeType() {
        return ServerType.BUNGEE;
    }

    @Override
    public FernCommands getBungeeInstance() {
        return FernCommands.getInstance();
    }

    @Override
    public io.github.fernthedev.fcommands.spigotclass.FernCommands getSpigotInstance() {
        return null;
    }
}
