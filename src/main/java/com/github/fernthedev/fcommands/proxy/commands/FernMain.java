package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fcommands.proxy.ProxyFileManager;
import com.github.fernthedev.fcommands.proxy.WhichFile;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.util.VersionUtil;

import javax.inject.Inject;
import java.io.IOException;

@CommandAlias("fernc")
public class FernMain extends BaseCommand {
    private static final String CONFIG_LIST = "all|ip|config";

    @Inject
    private ProxyFileManager proxyFileManager;

    @Inject
    private APIHandler apiHandler;

    @Inject
    private FernAPIPlugin plugin;

    @Description("Reload configs")
    @CommandCompletion(CONFIG_LIST)
    @Subcommand("config")
    @CommandPermission("fernc.config.reload")
    public void reloadConfig(CommandIssuer sender, WhichFile config) throws IOException {
        try {
            proxyFileManager.loadFiles(config);
        }catch (IOException e) {
            apiHandler.getMethods().getAbstractLogger().warn("&cUnable to reload files");
            throw e;
        }

        apiHandler.getMethods().getAbstractLogger().info("Successfully reloaded files");


        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded files");
    }

    @Description("Show help data")
    @CommandPermission("fernc.help")
    @HelpCommand
    @Default
    public void onHelp(CommandIssuer sender, CommandHelp help) {
        sender.sendMessage(ChatColor.BLUE + "Hello there. FernCommands are running. " + plugin.getPluginData().getVersion() + " (FernAPI: " + VersionUtil.getVersionData().getFernapi_version() + ")");
        help.showHelp();
    }
}
