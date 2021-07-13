package com.github.fernthedev.fcommands.bungee;


import com.github.fernthedev.fcommands.bungee.commands.BungeePluginList;
import com.github.fernthedev.fcommands.bungee.commands.ip.AltsBan;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration;
import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("unused")
public class FernCommands extends FernBungeeAPI {

    private static FernCommands instance;

    private static List<Runnable> runnableList = new ArrayList<>();

    public static List<Runnable> getRunnables() {
        return runnableList;
    }

    @Getter
    private static HookManager hookManager;


    @Override
   public void onEnable() {
        super.onEnable();

        instance = this;
        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR BUNGEECORD");

        if (!getDataFolder().exists()) {
            boolean mkdir = getDataFolder().mkdir();
            getLogger().info(mkdir + " folder make");
        }
        hookManager = new HookManager();

        getProxy().getPluginManager().registerCommand(this, new BungeePluginList());
        getProxy().getPluginManager().registerListener(this, new Events());


        //ADVANCEDBAN HOOK
        if (hookManager.hasAdvancedBan()) {
            getLogger().info(ChatColor.GREEN + "FOUND ADVANCEDBAN! HOOKING IN API");
            getProxy().getPluginManager().registerListener(this, new AltsBan());
        }

        getLogger().info("Registered fern nicks bungee channels.");

        getProxy().getPluginManager().registerListener(this, new PunishMOTD());


        PlatformAllRegistration.commonInit();

        getProxy().getPluginManager().registerListener(this, new BungeeServerMotdPing());

        getLogger().info("Registered fern nicks");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "SAVING FILES");
        FileManager.configSave(FileManager.getIpConfig());
        FileManager.configSave(FileManager.getSeenConfig());
        FileManager.configSave(FileManager.getConfigManager());
        FileManager.configSave(FileManager.getDeleteIPConfig());
        getLogger().info(ChatColor.GREEN + "FILED SUCCESSFULLY SAVED");

        super.onDisable();
        instance = this;
        getLogger().info(ChatColor.GREEN + "DISABLED FERNCOMMANDS FOR BUNGEECORD");

        HookManager.onDisable();
        instance = null;
    }




    public BaseComponent message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create()[0];
    }


    public void printInLog(Object classe, Object log) {
        getLogger().info("{" + classe.getClass().getName() + "} " + log);
    }




    public static FernCommands getInstance() {
        return instance;
    }
}
