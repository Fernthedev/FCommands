package com.github.fernthedev.fcommands.spigot;

import com.github.fernthedev.fcommands.spigot.commands.FernMain;
import com.github.fernthedev.fcommands.spigot.feature.FernProtection;
import com.github.fernthedev.fcommands.spigot.feature.Heaven;
import com.github.fernthedev.fcommands.spigot.gui.NameColor;
import com.github.fernthedev.fcommands.spigot.hooks.HookManager;
import com.github.fernthedev.fcommands.spigot.misc.*;
import com.github.fernthedev.fcommands.spigot.ncp.BungeeNCP;
import com.github.fernthedev.fcommands.spigot.ncp.NCPHandle;
import com.github.fernthedev.fcommands.universal.DBManager;
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration;
import com.github.fernthedev.fcommands.universal.UniversalMysql;
import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.google.inject.Injector;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;

public class FernCommands extends FernSpigotAPI {
    private SpigotFileManager spigotFileManager;

    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    private HookManager hookManager;

    private MessageListener messageListener;

    @Getter
    private DBManager databaseManager;

    @Override
    public void onEnable() {
        super.onEnable();

        injector = SpigotRegistration.spigotInit(this, injector);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        hookManager = injector.getInstance(HookManager.class);

        spigotFileManager = injector.getInstance(SpigotFileManager.class);
        String username = spigotFileManager.getValue("DBUsername", "root");
        String password = spigotFileManager.getValue("DBPass", "pass");
        String port = spigotFileManager.getValue("DBPort", "3306");
        String urlHost = spigotFileManager.getValue("DBHost", "localhost");
        String database = spigotFileManager.getValue("DB", "database");

        databaseManager = new DBManager(username, password, port, urlHost, database);
        UniversalMysql.setDatabaseManager(databaseManager);

        try {
            spigotFileManager.reloadConfig("all");
        } catch (IOException e) {
            getLogger().warning("Unable to load config");
        } catch (InvalidConfigurationException e) {
            getLogger().warning("Invalid Config");
        }


        messageListener = injector.getInstance(MessageListener.class);
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", messageListener);

        Messaging.sendRequest("GetServer");

        hookManager.registerPlugins();

        registerListener();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (hookManager.isNCPEnabled())
            NCPHandle.onDisable();
        // Unregister outgoing plugin channel if it's registered
        if (getServer().getMessenger().isOutgoingChannelRegistered(this, "BungeeCord"))
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        // Unregister incoming plugin channel if it's registered
        if (getServer().getMessenger().isIncomingChannelRegistered(this, "BungeeCord"))
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");

        // Kill any async tasks that may be left over
        getServer().getScheduler().cancelTasks(this);
        this.saveDefaultConfig();
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        getLogger().info(ChatColor.BLUE + "DISABLED FERNCOMMANDS");
    }

    public void registerListener() {
        try {
            spigotFileManager.reloadConfig("all");
        } catch (IOException e) {
            getLogger().warning("Unable to load config");
        } catch (InvalidConfigurationException e) {
            getLogger().warning("Invalid Config");
        }

        Injector injector = PlatformAllRegistration.getInjector();

        /*
        This disables the IG Door farm
         */
        if (spigotFileManager.getValue("NoIgDoorFarm", false))
            getServer().getPluginManager().registerEvents(new IronGolemDoorFarm(), this);
        /*
        This registers a NameColor and listener for tpbow
         */
        getServer().getPluginManager().registerEvents(injector.getInstance(RideBow.class), this);
        getServer().getPluginManager().registerEvents(injector.getInstance(Heaven.class), this);
        getServer().getPluginManager().registerEvents(injector.getInstance(FernProtection.class), this);


        /*
        REGISTERING DEFAULT COMMANDS
         */
        this.getCommand("fern").setExecutor(injector.getInstance(FernMain.class));



        /*
        This allows you to recieve NCP notifications on other servers using bungeecord Messaging
         */
        if (spigotFileManager.getValue("BungeeNCP", false))
            if (hookManager.isNCPEnabled()) {
                BungeeNCP bungeeNCP = injector.getInstance(BungeeNCP.class);

                NCPHandle.register(injector);
                messageListener.addListener(bungeeNCP);
                getServer().getPluginManager().registerEvents(bungeeNCP, this);
                getLogger().info("FOUND NOCHEATPLUS, ENABLING BUNGEECORD MODE");
            }
        /*
        This is a funny prank, when you go to sleep it burns you
         */
        if (spigotFileManager.getValue("BedFire", false))
            getServer().getPluginManager().registerEvents(new BedFire(), this);

        /*
        This makes you burn if you pick up lava or hurt you if you pick up cactus.
         */
        if (spigotFileManager.getValue("ItemBurn", false))
            getServer().getPluginManager().registerEvents(new LavaBurn(), this);

        /*
        This adds the skylands using SB-Skyland or any world you want and MultiVerse
        */
        if (spigotFileManager.getValue("Skylands", false)) {
            if (getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
                getLogger().info("Found Multiverse, checking to see skylands are enabled");
                if (getServer().getPluginManager().isPluginEnabled("SB-Skylands")) {
                    if (Bukkit.getWorld("skyland") != null)
                        getLogger().info("Found skylands, enabling enderpearl and overworld fall");
                    getServer().getPluginManager().registerEvents(new Skylands(), this);
                }
            }
        }

        if (spigotFileManager.getValue("NameColor", false)) {
            if ((hookManager.isVault() && getChat().isEnabled()) || hookManager.isNte()) {
                var nameColor = injector.getInstance(NameColor.class);
                getServer().getPluginManager().registerEvents(nameColor, this);
                getCommand("NameColor").setExecutor(nameColor);
            } else {
                getLogger().warning("Tried to start NameColor, but no compatible chat formatter (Vault) or nametag changer (NametagEdit) has been found. To work it needs one of these");
            }
        }
    }


    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }

    public static String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public boolean hasVaultPermission(Player p, String Permissione) {
        if (hookManager.isVault() && getPermissions().isEnabled()) {
            return p.hasPermission(Permissione) || getPermissions().has(p, Permissione);
        } else return p.hasPermission(Permissione);
    }

    public void setupVault() {
        setupEconomy();
        setupChat();
        setupPermissions();
    }

    public void addMessageListener(PluginMessageListener pluginMessageListener) {
        messageListener.addListener(pluginMessageListener);
    }
}
