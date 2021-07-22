package com.github.fernthedev.fcommands.velocity;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration;
import com.github.fernthedev.fcommands.velocity.commands.VelocityPluginList;
import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;

@Plugin(id = "fern_commands", name = "FernCommands", version = "${version}",
        description = "A suite of stuff for Fern server", authors = {"Fernthedev"},
        dependencies = @Dependency(id = "preference_manager")
)
public class FernCommands extends FernVelocityAPI {

    @Inject
    public FernCommands(ProxyServer server, Logger logger, PluginContainer pluginContainer) {
        super(server, logger, pluginContainer);
    }

    @Subscribe
    @Override
    public void onProxyInitialization(ProxyInitializeEvent event) {
        super.onProxyInitialization(event);

        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR VELOCITY");

        File dataFolder = dataDirectory.toFile();

        if (!dataFolder.exists()) {
            boolean mkdir = dataFolder.mkdir();
            getLogger().info(mkdir + " folder make");
        }


        server.getCommandManager().register("vplugins",new VelocityPluginList(server));
        server.getEventManager().register(this, new Events());

        PlatformAllRegistration.commonInit();

        server.getEventManager().register(this, new VelocityServerMotdPing());


        getLogger().info("Registered fern nicks");

    }

    @Override
    public void onProxyStop(ProxyShutdownEvent event) {
        super.onProxyStop(event);
        getLogger().info(ChatColor.GREEN + "SAVING FILES");
        FileManager.configSave(FileManager.getIpConfig());
        FileManager.configSave(FileManager.getSeenConfig());
        FileManager.configSave(FileManager.getConfigManager());
        FileManager.configSave(FileManager.getDeleteIPConfig());
        getLogger().info(ChatColor.GREEN + "FILED SUCCESSFULLY SAVED");

        getLogger().info(ChatColor.GREEN + "DISABLED FERNCOMMANDS FOR BUNGEECORD");
    }
}
