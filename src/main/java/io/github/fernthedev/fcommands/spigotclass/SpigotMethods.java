package io.github.fernthedev.fcommands.spigotclass;

import io.github.fernthedev.fcommands.Universal.MethodInterface;
import io.github.fernthedev.fcommands.Universal.ServType;

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
    public ServType getServeType() {
        return ServType.Bukkit;
    }
}
