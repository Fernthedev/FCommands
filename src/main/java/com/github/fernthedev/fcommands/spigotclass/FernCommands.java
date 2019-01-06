package com.github.fernthedev.fcommands.spigotclass;

import com.github.fernthedev.fcommands.Universal.Channels;
import com.github.fernthedev.fcommands.spigotclass.commands.FernMain;
import com.github.fernthedev.fcommands.spigotclass.entity.NoAI;
import com.github.fernthedev.fcommands.spigotclass.gui.namecolor;
import com.github.fernthedev.fcommands.spigotclass.hooks.HookManager;
import com.github.fernthedev.fcommands.spigotclass.misc.*;
import com.github.fernthedev.fcommands.spigotclass.ncp.NCPHandle;
import com.github.fernthedev.fcommands.spigotclass.ncp.bungeencp;
import com.github.fernthedev.fcommands.spigotclass.ncp.cooldown;
import com.github.fernthedev.fcommands.spigotclass.placeholderapi.HookPlaceHolderAPI;
import com.github.fernthedev.fcommands.spigotclass.shop.ChestImport;
import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.google.gson.Gson;
import lombok.NonNull;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FernCommands extends FernSpigotAPI {
    private FileConfiguration config;

    private static FernCommands instance;
    public static String SERVER_NAME;
    private static Gson gson;
    private boolean isStaffMemberOnline = false;
    private static com.github.fernthedev.fcommands.spigotclass.ncp.cooldown cooldown;
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    public static HookManager getHookManager() {
        return hookManager;
    }

    private static HookManager hookManager;
    private static Connection connection;

    private MessageListener messageListener;

    private static List<BukkitRunnable> runnables = new ArrayList<>();

    public static List<BukkitRunnable> getRunnables() {
        return runnables;
    }


    public void onEnable() {
        super.onEnable();
        
        instance = this;
        config = this.getConfig();
        gson = new Gson();
        SERVER_NAME = null;
        cooldown = new cooldown();

        hookManager = new HookManager();


        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, Channels.PlaceHolderBungeeChannel);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, Channels.PlaceHolderBungeeChannel, new HookPlaceHolderAPI() );

        getLogger().info("Connecting to mysql");
        DatabaseHandler.setup();

        connection = DatabaseHandler.getConnection();

        try {
            FilesManager.getInstance().reloadConfig("all");
        } catch (IOException e) {
            getLogger().warning("Unable to load config");
        } catch (InvalidConfigurationException e) {
            getLogger().warning("Invalid Config");
        }


        messageListener = new MessageListener();
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", messageListener);

        messaging.sendRequest("GetServer");

        hookManager.registerPlugins();

        registerListener();
    }















    @NonNull
    public static FernCommands getInstance() {
        return instance;
    }

    @NonNull
    public static Gson getGson() {
        return gson;
    }

    public void onDisable() {
        if(hookManager.isNCPEnabled())
        NCPHandle.onDisable();
        // Unregister outgoing plugin channel if it's registered
        if (this.getServer().getMessenger().isOutgoingChannelRegistered(this, "BungeeCord"))
            this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        // Unregister incoming plugin channel if it's registered
        if (this.getServer().getMessenger().isIncomingChannelRegistered(this, "BungeeCord"))
            this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");

        DatabaseHandler.getScheduler().cancelTasks(this);
        // invoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (connection!=null && !connection.isClosed()){ //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Kill any async tasks that may be left over
        getServer().getScheduler().cancelTasks(instance);
        SERVER_NAME = null;
        gson = null;
        isStaffMemberOnline = false;
        cooldown = null;
        instance = null;
        this.saveDefaultConfig();
        config = null;
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        getLogger().info(ChatColor.BLUE + "DISABLED FERNCOMMANDS");
    }


    public void checkForStaffMembers() {
        for (Player onlineplayer : this.getServer().getOnlinePlayers()) {
            if (!onlineplayer.hasPermission("nocheatplus.notify"))
                continue;

            isStaffMemberOnline = true;
            break;
        }
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    @NonNull
    public static cooldown getCooldownManager() {
        return cooldown;
    }

    public boolean isStaffMemberOnline() {
        return this.isStaffMemberOnline;
    }

    public void registerListener() {
        try {
            FilesManager.getInstance().reloadConfig("all");
        } catch (IOException e) {
            getLogger().warning("Unable to load config");
        } catch (InvalidConfigurationException e) {
            getLogger().warning("Invalid Config");
        }



        /*
        This disables damage from enderpearls being thrown
         */
        if (config.getBoolean("nodmgepearl"))
            this.getServer().getPluginManager().registerEvents(new godpearl(), this);
        /*
        This disables the IG Door farm
         */
        if (config.getBoolean("NoIgDoorFarm"))
            this.getServer().getPluginManager().registerEvents(new igdoorfarm(), this);
        /*
        This registers a namecolor and listener for tpbow
         */
        if (config.getBoolean("tpbow")) {
            this.getServer().getPluginManager().registerEvents(new ridebow(), this);
            this.getCommand("craftrb").setExecutor(new ridebow());
        }


        /*
        REGISTERING DEFAULT COMMANDS
         */
        //this.getCommand("fern").setExecutor(new ReloadConfig());
        //this.getCommand("fern").setExecutor(new HookManager());
        this.getCommand("fern").setExecutor(new FernMain());


        /*
        This allows you to recieve NCP notifications on other servers using bungeecord messaging
         */
        if (config.getBoolean("BungeeNCP"))
            //if (this.getServer().getPluginManager().getPlugin("NoCheatPlus") != null) {
            if(hookManager.isNCPEnabled()) {
                NCPHandle.register();
                messageListener.addListener(new bungeencp());
               // this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new bungeencp());
                this.getServer().getPluginManager().registerEvents(new bungeencp(), this);
                getLogger().info("FOUND NOCHEATPLUS, ENABLING BUNGEECORD MODE");
            }
        /*
        This is a funny prank, when you go to sleep it burns you
         */
        if (config.getBoolean("BedFire"))
            this.getServer().getPluginManager().registerEvents(new BedFire(), this);

        /*
        This makes you burn if you pick up lava or hurt you if you pick up cactus.
         */
        if (config.getBoolean("ItemBurn"))
            this.getServer().getPluginManager().registerEvents(new LavaBurn(), this);

        if(FilesManager.getInstance().getValue("AddShop",false)) {
            ChestImport chestImport = new ChestImport();
            this.getCommand("fshop").setExecutor(chestImport);
        }
        /*
          If MCMMO and NTE are enabled, when MCMMO levels up, nametag prefixes and suffixes get messed up.
          This is to prevent that, this reloads NTE every time MCMMO levels up
         */
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO")) {
            hookManager.hook();
            if (hookManager.isNTEEnabled()) {
                    getLogger().info("FOUND MCMMO AND NAMETAGEDIT IN PLUGINS, ENABLING AUTO RELOAD");
                    this.getServer().getPluginManager().registerEvents(new NTEmcMMO(), this);
            }
        }

        /*
        This adds the skylands using SB-Skyland or any world you want and MultiVerse
        */
        if(config.getBoolean("Skylands")) {
            if (this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
                getLogger().info("Found Multiverse, checking to see skylands are enabled");
                if (this.getServer().getPluginManager().isPluginEnabled("SB-Skylands")) {
                    if (Bukkit.getWorld("skyland") != null)
                        getLogger().info("Found skylands, enabling enderpearl and overworld fall");
                    this.getServer().getPluginManager().registerEvents(new skylands(), this);
                }
            }
        }

        if(config.getBoolean("NoAIonSpawn")) {
            this.getServer().getPluginManager().registerEvents(new NoAI(),this);
        }

        if(config.getBoolean("NameColor")) {
            if((hookManager.isVaultEnabled() && getChat().isEnabled()) || hookManager.isNTEEnabled()) {
             this.getServer().getPluginManager().registerEvents(new namecolor(),this);
             this.getCommand("namecolor").setExecutor(new namecolor());
            }else{
                getLogger().warning("Tried to start NameColor, but no compatible chat formatter (Vault) or nametag changer (NametagEdit) has been found. To work it needs one of these");
            }
        }

        //this.getServer().getPluginManager().registerEvents(new UUIDSpoofChecker(),this);
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
        return ChatColor.translateAlternateColorCodes('&',message);
    }


    public static boolean hasVaultPermission(Player p,String Permissione) {
        if(hookManager.isVaultEnabled() && getPermissions().isEnabled()) {
            return p.hasPermission(Permissione) || getPermissions().has(p, Permissione);
        }else return p.hasPermission(Permissione);
    }

    public void setupVault() {
        setupEconomy();
        setupChat();
        setupPermissions();
    }

    public void addMessageListener(PluginMessageListener pluginMessageListener) {
        messageListener.addListener(pluginMessageListener);
    }

    @Override
    public void cancelTask(int id) {
        getServer().getScheduler().cancelTask(id);
    }
}
