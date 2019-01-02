package com.github.fernthedev.fcommands.spigotclass.hooks;

import com.earth2me.essentials.Essentials;
import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import com.github.fernthedev.fcommands.spigotclass.placeholderapi.VanishPlaceholder;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.github.fernthedev.fcommands.spigotclass.nick.NickJoin;
import com.github.fernthedev.fcommands.spigotclass.nick.NickReload;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class HookManager {


    private static boolean isVault;
    private static boolean isNTE;
    private static boolean isPlaceHolderAPI;
    private static boolean isWorldGuard;
    private static boolean isEssentials;

    private WorldGuardPlugin worldGuardPlugin;
    private Essentials essentials;

    private boolean useMcMMO;

    //CHECK IF COMPATIBLE PLUGINS ARE ENABLED

    public boolean isVaultEnabled() {
        return isVault;
    }
    public boolean isNTEEnabled() {
        return isNTE;
    }

    public boolean isNCPEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus");
    }

    public boolean isIsWorldGuard() {
        return isWorldGuard;
    }


    public boolean isEssentials() {
        return isEssentials;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public boolean mcmmoEnabled() {
        return useMcMMO;
    }

    /**
     * This registers if any compatible plugins are enabled for other methods
     */
    public void registerPlugins() {
        /*
          Checks for vault
         */
        isVault = false;
        if(this.getServer().getPluginManager().isPluginEnabled("Vault")) {
            isVault = true;
            FernCommands.getInstance().setupVault();
            getLogger().info("HOOKED VAULT ECONOMY PERMISSIONS AND CHAT");
        }

        /*
         Checks for NametagEdit
         */
        isNTE = this.getServer().getPluginManager().isPluginEnabled("NametagEdit");
        if(isNTE)
            getLogger().info("HOOKED NAMETAGEDIT API");

        isPlaceHolderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if(isPlaceHolderAPI) {
            //Registering placeholder will be use here
            new VanishPlaceholder().register();
            getLogger().info("HOOKED PLACEHOLDERAPI");
            //messageListener.addListener(new HookPlaceHolderAPI());
            getLogger().info("HOOKED PLACEHOLDERAPI BUNGEE MESSAGING");
        }

        isEssentials = Bukkit.getPluginManager().isPluginEnabled("Essentials");
        if(isEssentials) {
            FernCommands.getInstance().addMessageListener(new NickReload());
            FernCommands.getInstance().getServer().getPluginManager().registerEvents(new NickJoin(),FernCommands.getInstance());
        }
    }

    public void hook() {
        useMcMMO = Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO");
        worldGuardPlugin = getWorldGuard();
    }


    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            isWorldGuard = false;
            return null; // Maybe you want throw an exception instead
        }

        isWorldGuard = true;
        return (WorldGuardPlugin) plugin;
    }

    private Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }

    private Server getServer() {
        return FernCommands.getInstance().getServer();
    }



}
