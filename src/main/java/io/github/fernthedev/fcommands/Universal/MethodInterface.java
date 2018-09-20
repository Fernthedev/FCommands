package io.github.fernthedev.fcommands.Universal;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;

import java.util.logging.Logger;

public interface MethodInterface {

    Logger getLogger();

    Object getInstance();

    ServerType getServeType();

    FernCommands getBungeeInstance();

    io.github.fernthedev.fcommands.spigotclass.FernCommands getSpigotInstance();
}

