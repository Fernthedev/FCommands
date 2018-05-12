package io.github.fernthedev.fcommands.spigotclass;

import com.google.gson.Gson;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;
import io.github.fernthedev.fcommands.spigotclass.commands.fernmain;
import io.github.fernthedev.fcommands.spigotclass.gui.namecolor;
import io.github.fernthedev.fcommands.spigotclass.ncp.bungeencp;
import io.github.fernthedev.fcommands.spigotclass.ncp.cooldown;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.logging.Logger;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class spigot extends JavaPlugin {
    private FileConfiguration config;
    private boolean useMcMMO;
    private static spigot instance;
    public static String SERVER_NAME;
    private static Gson gson;
    private boolean isStaffMemberOnline = false;
    private static io.github.fernthedev.fcommands.spigotclass.ncp.cooldown cooldown;
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    private static boolean isVault;
    private static boolean isNTE;
    @Override
    public void onEnable() {
        instance = this;
        config = this.getConfig();
        getLogger().info("Hey! This is just for you to know that the plugin is enabled!");
        //BedFire.run();
        //Bukkit.getServer().getPluginManager().registerEvents(new BedFire(), this);
        //this.getServer().getPluginManager().registerEvents(new BedFire(), this);
        //this.getServer().getPluginManager().registerEvents(new LavaBurn(), this);
        gson = new Gson();
        SERVER_NAME = null;
        cooldown = new cooldown();
       // BedFire.onEnable();
      //  LavaBurn.onEnable();
        //new ridebow().craft();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        messaging.sendRequest("GetServer");
        registerPlugins();
        //FilesManager.getInstance().setDefault();
        try {
            FilesManager.getInstance().reloadConfig("all");
        } catch (IOException e) {
            getLogger().warning("Unable to load config");
        } catch (InvalidConfigurationException e) {
            getLogger().warning("Invalid Config");
        }
        registerListener();
        //this.getCommand("fboot").setExecutor(new WaterBoots());






        //this.getServer().getPluginManager().registerEvents(new RidePlayer(), this);
        //RidePlayer.onEnable();
        //BedFire bedfire = new BedFire();
        //this.getCommand("fbedcheck").setExecutor(new BedFire());
        /*if (getServer().getPluginManager().isPluginEnabled(bedfire)) {
            getLogger().info("UNABLE TO START BEDFIRE!");
        }else{
            getLogger().info("ENABLED BEDFIRE!");
        }*/

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
            setupEconomy();
            setupPermissions();
            setupChat();
            getLogger().info("HOOKED VAULT ECONOMY PERMISSIONS AND CHAT");
        }

        /*
         Checks for NametagEdit
         */
        isNTE = this.getServer().getPluginManager().isPluginEnabled("NametagEdit");
        if(isNTE)
            getLogger().info("HOOKED NAMETAGEDIT API");
    }






    public boolean mcmmoEnabled() {
        return useMcMMO;
    }

    public void hook() {
        useMcMMO = Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO");
    }

    @Nonnull
    public static spigot getInstance() {
        return instance;
    }

    @Nonnull
    public static Gson getGson() {
        return gson;
    }

    public void onDisable() {
        if (NCPHookManager.getHooksByName(this.getName()) != null)
            NCPHookManager.removeHooks(this.getName());
        // Unregister outgoing plugin channel if it's registered
        if (this.getServer().getMessenger().isOutgoingChannelRegistered(this, "BungeeCord"))
            this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        // Unregister incoming plugin channel if it's registered
        if (this.getServer().getMessenger().isIncomingChannelRegistered(this, "BungeeCord"))
            this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
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

   // @Override
    // FileConfiguration getConfig() {
    //    return config;
   // }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    @Nonnull
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
        //this.getCommand("fern").setExecutor(new reloadconfig());
        //this.getCommand("fern").setExecutor(new hooks());
        this.getCommand("fern").setExecutor(new fernmain());


        /*
        This allows you to recieve NCP notifications on other servers using bungeecord messaging
         */
        if (config.getBoolean("BungeeNCP"))
            //if (this.getServer().getPluginManager().getPlugin("NoCheatPlus") != null) {
            if(isNCPEnabled()) {
                NCPHookManager.addHook(CheckType.values(), new bungeencp());
                this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new bungeencp());
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

        /*
          If MCMMO and NTE are enabled, when MCMMO levels up, nametag prefixes and suffixes get messed up.
          This is to prevent that, this reloads NTE every time MCMMO levels up
         */
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO")) {
            hook();
            if (isNTE) {
                {
                    getLogger().info("FOUND MCMMO AND NAMETAGEDIT IN PLUGINS, ENABLING AUTO RELOAD");
                    this.getServer().getPluginManager().registerEvents(new NTEmcMMO(), this);
                }
            }
        }

        /*
        This adds the skylands using SB-Skyland or any world you want and MultiVerse
        */
        if(config.getBoolean("Skylands"))
        if (this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
            getLogger().info("Found Multiverse, checking to see skylands are enabled");
            if (this.getServer().getPluginManager().isPluginEnabled("SB-Skylands")) {
                if (Bukkit.getWorld("skyland") != null)
                    getLogger().info("Found skylands, enabling enderpearl and overworld fall");
                this.getServer().getPluginManager().registerEvents(new skylands(), this);
            }
        }

        if(config.getBoolean("NameColor")) {
            if((isVault && getChat().isEnabled()) || isNTE) {
             this.getServer().getPluginManager().registerEvents(new namecolor(),this);
             this.getCommand("namecolor").setExecutor(new namecolor());
            }else{
                getLogger().warning("Tried to start NameColor, but no compatible chat formatter (Vault) or nametag changer (NametagEdit) has been found. To work it needs one of these");
            }
        }
    }

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









    private boolean setupEconomy() {
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

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
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
        if(isVault && getPermissions().isEnabled()) {
            return p.hasPermission(Permissione) || getPermissions().has(p, Permissione);
        }else return p.hasPermission(Permissione);
    }
}
