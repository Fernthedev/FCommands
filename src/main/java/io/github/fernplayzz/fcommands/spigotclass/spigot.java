package io.github.fernplayzz.fcommands.spigotclass;

import com.google.gson.Gson;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;
import io.github.fernplayzz.fcommands.spigotclass.ncp.bungeencp;
import io.github.fernplayzz.fcommands.spigotclass.ncp.cooldown;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class spigot extends JavaPlugin {
    public FileConfiguration config = this.getConfig();
    private boolean useMcMMO;
    private static spigot instance;
    public static String SERVER_NAME;
    private static Gson gson;
    private boolean isStaffMemberOnline = false;
    private static cooldown cooldown;
    @Override
    public void onEnable() {
        config = this.getConfig();
        this.getConfig();
        instance = this;
        getLogger().info("Hey! This is just for you to know that the plugin is enabled!");
        //BedFire.run();
        //Bukkit.getServer().getPluginManager().registerEvents(new BedFire(), this);
        //this.getServer().getPluginManager().registerEvents(new BedFire(), this);
        //this.getServer().getPluginManager().registerEvents(new LavaBurn(), this);
        gson = new Gson();
        SERVER_NAME = null;
        cooldown = new cooldown();
        BedFire.onEnable();
        LavaBurn.onEnable();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        messaging.sendRequest("GetServer");
        this.getServer().getPluginManager().registerEvents(new WaterBoots(), this);
        WaterBoots.onEnable();
        this.getCommand("fboot").setExecutor(new WaterBoots());
        if (this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
            getLogger().info("Found Multiverse, checking to see skylands are enabled");
            if (this.getServer().getPluginManager().isPluginEnabled("SB-Skylands")) {
                if (Bukkit.getWorld("skyland") != null)
                    getLogger().info("Found skylands, enabling enderpearl and overworld fall");
                this.getServer().getPluginManager().registerEvents(new skylands(), this);
            }
        }
        if(this.getServer().getPluginManager().getPlugin("NoCheatPlus") != null) {
            NCPHookManager.addHook(CheckType.values(), new bungeencp());
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new bungeencp());
            this.getServer().getPluginManager().registerEvents(new bungeencp(), this);
            getLogger().info("FOUND NOCHEATPLUS, ENABLING BUNGEECORD MODE");
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO")) {
            hook();
            Bukkit.getServer().getPluginManager().isPluginEnabled("NametagEdit");
            {
                getLogger().info("FOUND MCMMO AND NAMETAGEDIT IN PLUGINS, ENABLING AUTO RELOAD");
                this.getServer().getPluginManager().registerEvents(new NTEmcMMO(), this);
            }
        }
        this.getServer().getPluginManager().registerEvents(new godpearl(), this);
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

    public boolean mcmmoEnabled() {
        return useMcMMO;
    }

    public void hook() {
        useMcMMO = Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO");
    }


    protected void infolog(String text) {
        if (!text.isEmpty()) {
            getLogger().info(text);
        }
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
    }


    public void checkForStaffMembers() {
        for (Player onlineplayer : this.getServer().getOnlinePlayers()) {
            if (!onlineplayer.hasPermission("nocheatplus.notify"))
                continue;

            isStaffMemberOnline = true;
            break;
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

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
}
