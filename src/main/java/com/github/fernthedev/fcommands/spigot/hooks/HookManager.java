package com.github.fernthedev.fcommands.spigot.hooks;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.nick.NickManager;
import com.github.fernthedev.fcommands.spigot.placeholderapi.VanishPlaceholder;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.google.inject.Injector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

@Singleton
@Getter
public class HookManager {

    @Inject
    private FernCommands fernCommands;

    @Inject
    private Server server;

    @Inject
    private Injector injector;

    @Inject
    private APIHandler apiHandler;

    private boolean vault;
    private boolean nte;
    private boolean placeHolderAPI;
    private boolean worldGuard;
    private boolean essentials;

    private WorldGuardPlugin worldGuardPlugin;

    //CHECK IF COMPATIBLE PLUGINS ARE ENABLED

    public boolean isNCPEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus");
    }

    /**
     * This registers if any compatible plugins are enabled for other methods
     */
    public void registerPlugins() {
        Logger logger = fernCommands.getLogger();

        /*
          Checks for vault
         */
        vault = false;
        if(this.getServer().getPluginManager().isPluginEnabled("Vault")) {
            vault = true;
            fernCommands.setupVault();
            logger.info("HOOKED VAULT ECONOMY PERMISSIONS AND CHAT");
        }

        /*
         Checks for NametagEdit
         */
        nte = this.getServer().getPluginManager().isPluginEnabled("NametagEdit");
        if(nte)
            logger.info("HOOKED NAMETAGEDIT API");

        placeHolderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if(placeHolderAPI) {
            //Registering placeholder will be use here
            new VanishPlaceholder().register();

            logger.info("HOOKED PLACEHOLDERAPI");
            //messageListener.addListener(new HookPlaceHolderAPI());
            logger.info("HOOKED PLACEHOLDERAPI BUNGEE MESSAGING");
        }

        essentials = Bukkit.getPluginManager().isPluginEnabled("Essentials");
        apiHandler.getLogger().info("Essentials status: {}", essentials);
        if(essentials) {
            fernCommands.getServer().getPluginManager().registerEvents(injector.getInstance(NickManager.class), fernCommands);
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

}
