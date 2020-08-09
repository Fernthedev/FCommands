package com.github.fernthedev.fcommands.spigot.hooks;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.nick.NickManager;
import com.github.fernthedev.fcommands.spigot.placeholderapi.VanishPlaceholder;
import com.github.fernthedev.fcommands.universal.NickNetworkManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class HookManager {


    @Getter
    private static boolean vault;

    @Getter
    private static boolean nte;


    @Getter
    private static boolean placeHolderAPI;

    @Getter
    private static boolean worldGuard;

    @Getter
    private static boolean essentials;

    @Getter
    private WorldGuardPlugin worldGuardPlugin;

    //CHECK IF COMPATIBLE PLUGINS ARE ENABLED

    public boolean isNCPEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus");
    }

    /**
     * This registers if any compatible plugins are enabled for other methods
     */
    public void registerPlugins() {
        /*
          Checks for vault
         */
        vault = false;
        if(this.getServer().getPluginManager().isPluginEnabled("Vault")) {
            vault = true;
            FernCommands.getInstance().setupVault();
            getLogger().info("HOOKED VAULT ECONOMY PERMISSIONS AND CHAT");
        }

        /*
         Checks for NametagEdit
         */
        nte = this.getServer().getPluginManager().isPluginEnabled("NametagEdit");
        if(nte)
            getLogger().info("HOOKED NAMETAGEDIT API");

        placeHolderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if(placeHolderAPI) {
            //Registering placeholder will be use here
            new VanishPlaceholder().register();

            getLogger().info("HOOKED PLACEHOLDERAPI");
            //messageListener.addListener(new HookPlaceHolderAPI());
            getLogger().info("HOOKED PLACEHOLDERAPI BUNGEE MESSAGING");
        }

        essentials = Bukkit.getPluginManager().isPluginEnabled("Essentials");
        if(essentials) {
            Universal.getMessageHandler().registerMessageHandler(new NickNetworkManager());
//            FernCommands.getInstance().addMessageListener(new NickManager());
            FernCommands.getInstance().getServer().getPluginManager().registerEvents(new NickManager(), FernCommands.getInstance());
        }
    }

    public void hook() {
        worldGuardPlugin = getWorldGuardInstance();
    }


    private WorldGuardPlugin getWorldGuardInstance() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            worldGuard = false;
            return null; // Maybe you want throw an exception instead
        }

        worldGuard = true;
        return (WorldGuardPlugin) plugin;
    }

    private Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }

    private Server getServer() {
        return FernCommands.getInstance().getServer();
    }

}
