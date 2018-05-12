package io.github.fernthedev.fcommands.bungeeclass;


import io.github.fernthedev.fcommands.bungeeclass.commands.fernmain;
import io.github.fernthedev.fcommands.bungeeclass.commands.seen;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

//@SuppressWarnings("unused")
public class FernCommands extends Plugin {

    //public static File pluginFolder;
    private static File ipfile;
    private static File seenfile;
   // private static Configuration IpDataConfig;
    //private static Configuration configuration;
    //private static ConfigurationProvider configp;

    //private static File configfile;
    private static FernCommands instance;


    @Override
   public void onEnable() {
        instance = this;
        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = new File(getDataFolder(), "ipdata.yml");
        seenfile = new File(getDataFolder(), "seen.yml");
        //configfile = new File(getDataFolder(), "config.yml");
        //configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
        new hooks();
        new FileManager();
        if (!getDataFolder().exists()) {
            boolean mkdir = getDataFolder().mkdir();
            System.out.println(mkdir);
        }

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // config.getKeys().add("Motd:");
        }
        try {
            FileManager.getInstance().loadFiles("config",false);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //CREATING SEEN FILE
        FileManager.getInstance().createseenFile();
        try {
            FileManager.getInstance().loadFiles("seen",false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        //File ipfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        //File seenfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");


        getProxy().getPluginManager().registerListener(this, new servermaintenance());
        //SEEN COMMAND
        getProxy().getPluginManager().registerCommand(this, new seen());
        getProxy().getPluginManager().registerListener(this, new seen());


        //ADVANCEDBAN HOOK
        if (hooks.getInstance().hasIsAdvancedBan()) {
            getProxy().getLogger().info(ChatColor.GREEN + "FOUND ADVANCEDBAN! HOOKING IN API");
            try {
                FileManager.getInstance().createipFile();
                //Configuration IpDataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
                //FileManager.getInstance().loadFile(ipfile);
                FileManager.getInstance().loadFiles("ip",true);
            } catch (IOException e) {
                getProxy().getLogger().warning("Unable to load ips. ");
            }
            getProxy().getPluginManager().registerListener(this, new punishmotd());
        } else {
            getProxy().getLogger().info(ChatColor.YELLOW + "ADVANCEDBAN NOT FOUND, DISABLING PUNISHMOTD");
        }

        //MAIN FERN COMMAND MANAGER
        getProxy().getPluginManager().registerCommand(this, new fernmain());
        run();

    }



    @Override
    public void onDisable(){
        instance = this;
        getLogger().info(ChatColor.AQUA + "DISABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = null;
        seenfile = null;
        fernmain.onDisable();
        seen.onDisable();
        hooks.onDisable();
        FileManager.onDisable();
        instance = null;
    }



    public static File getIpfile() {
        return ipfile;
    }




    public BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

    private void run() {
        getProxy().getScheduler().schedule(this, () -> {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            } catch (IOException e) {
                getLogger().warning("unable to reload config");
            }
        }, 3L, TimeUnit.SECONDS);
    }


    public File getSeenfile() {
        return seenfile;
    }

    @Nonnull
    public static FernCommands getInstance() {
        return instance;
    }
}
