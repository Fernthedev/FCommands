package io.github.fernthedev.fcommands.bungeeclass;

import io.github.fernthedev.fcommands.Universal.MethodInterface;

import java.util.logging.Logger;

public class BungeeMethods implements MethodInterface {
    @Override
    public Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }
}
