package io.github.fernthedev.fcommands.bungeeclass;

import io.github.fernthedev.fcommands.Universal.MethodInterface;
import io.github.fernthedev.fcommands.Universal.ServType;

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
    public ServType getServeType() {
        return ServType.BUNGEE;
    }
}
