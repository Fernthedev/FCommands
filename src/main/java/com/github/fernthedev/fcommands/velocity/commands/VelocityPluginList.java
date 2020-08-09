package com.github.fernthedev.fcommands.velocity.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class VelocityPluginList implements Command {

    private final ProxyServer proxyServer;

    public VelocityPluginList(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    /**
     * Executes the command for the specified {@link CommandSource}.
     *
     * @param source the source of this command
     * @param args   the arguments for this command
     */
    @Override
    public void execute(CommandSource source, @NotNull @NonNull String[] args) {
        if (!source.hasPermission("bungee.plugins")) {
            source.sendMessage(TextComponent.of("You do not have permission for this").color(TextColor.RED));
            return;
        }

        @NonNull TextComponent baseComponent = TextComponent.of("&aPlugins:");

        for (PluginContainer plugin : proxyServer.getPluginManager().getPlugins()) {
            baseComponent.append(TextComponent.of(" ").color(TextColor.GRAY));
            baseComponent.append(TextComponent.of(plugin.getDescription().getName().orElse("null")));
            baseComponent.append(TextComponent.of(" &8{" + plugin.getDescription().getAuthors() + "}; "));
        }

        source.sendMessage(baseComponent);
    }
}
