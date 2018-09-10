package io.github.fernthedev.fcommands.spigotclass;

import io.github.fernthedev.fcommands.Universal.MethodInterface;

import java.util.logging.Logger;

public class SpigotMethods implements MethodInterface {
    @Override
    public Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }
}
