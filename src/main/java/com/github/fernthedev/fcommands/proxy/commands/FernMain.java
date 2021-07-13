package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.util.VersionUtil;

import java.io.IOException;

@CommandAlias("fernc")
public class FernMain extends BaseCommand {
    private static final String configList = "all|ip|config";

    @Description("Reload configs")
    @CommandCompletion(configList)
    @Subcommand("config")
    @CommandPermission("fernc.config.reload")
    public void reloadConfig(CommandIssuer sender, String config) throws IOException {
        try {
            FileManager.loadFiles(FileManager.WhichFile.fromString(config), false);
        }catch (IOException e) {
            Universal.getMethods().getAbstractLogger().warn("&cUnable to reload files");
            throw e;
        }

        Universal.getMethods().getAbstractLogger().info("Successfully reloaded files");


        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded files");
    }

    @Description("Show help data")
    @CommandPermission("fernc.help")
    @HelpCommand
    @Default
    public void onHelp(CommandIssuer sender, CommandHelp help) {
        sender.sendMessage(ChatColor.BLUE + "Hello there. FernCommands are running. " + Universal.getPlugin().getPluginData().getVersion() + " (FernAPI: " + VersionUtil.getVersionData().getFernapi_version() + ")");
        help.showHelp();
    }
}
