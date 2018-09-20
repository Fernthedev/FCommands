package io.github.fernthedev.fcommands.spigotclass.methods;

import io.github.fernthedev.fcommands.Universal.MethodInterface;
import io.github.fernthedev.fcommands.Universal.ServerType;
import io.github.fernthedev.fcommands.spigotclass.FernCommands;

import java.util.logging.Logger;

public class SpigotMethods implements MethodInterface {
    @Override
    public Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }

    @Override
    public Object getInstance() {
        return this;
    }

    @Override
    public ServerType getServeType() {
        return ServerType.BUKKIT;
    }

    @Override
    public io.github.fernthedev.fcommands.bungeeclass.FernCommands getBungeeInstance() {
        return null;
    }

    @Override
    public FernCommands getSpigotInstance() {
        return FernCommands.getInstance();
    }
}
