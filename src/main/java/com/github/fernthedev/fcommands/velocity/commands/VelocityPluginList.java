package com.github.fernthedev.fcommands.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class VelocityPluginList implements SimpleCommand {

    private final ProxyServer proxyServer;

    public VelocityPluginList(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    /**
     * Executes the command for the specified {@link CommandSource}.
     *
     */
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!source.hasPermission("bungee.plugins")) {
            source.sendMessage(Component.text("You do not have permission for this").color(NamedTextColor.RED));
            return;
        }

        @NonNull TextComponent baseComponent = LegacyComponentSerializer.legacyAmpersand().deserialize("&aPlugins:");

        for (PluginContainer plugin : proxyServer.getPluginManager().getPlugins()) {
            baseComponent = baseComponent.append(Component.text(" ").color(NamedTextColor.GRAY))
            .append(Component.text(plugin.getDescription().getName().orElse("null")))
            .append(LegacyComponentSerializer.legacyAmpersand().deserialize(" &8{" + plugin.getDescription().getAuthors() + "}; "));
        }

        source.sendMessage(baseComponent);
    }
}
