package io.github.fernthedev.fcommands.bungeeclass;


import io.github.fernthedev.fcommands.bungeeclass.commands.fernmain;
import io.github.fernthedev.fcommands.bungeeclass.commands.seen;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

//@SuppressWarnings("unused")
public class bungee extends Plugin {

    //public static File pluginFolder;
    private static File ipfile;
    private static File seenfile;
   // private static Configuration IpDataConfig;
    private static Configuration seendataConfig;
    private static Configuration configuration;
    private static ConfigurationProvider configp;
    private static Configuration config;
    private static Configuration ipconfig;
    private static Configuration seenconfig;
    private static File configfile;
    private static bungee instance;


    @Override
   public void onEnable() {
        instance = this;
        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = new File(getDataFolder(), "ipdata.yml");
        seenfile = new File(getDataFolder(), "seen.yml");
        configfile = new File(getDataFolder(), "config.yml");
        configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
        new hooks();
        new fileconfig();


        if (!getDataFolder().exists()) {
            boolean mkdir = getDataFolder().mkdir();
        }

        //File cff = new File("config.yml");
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
                //boolean success = file.renameTo(cff);
               // getLogger().info("Config renaming value: " + success + " removing not named config: " + deleted);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // config.getKeys().add("Motd:");
        }


        fileconfig.getInstance().createseenFile();
        File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        File ipfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        File seenfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");


        getProxy().getPluginManager().registerListener(this, new servermaintenance());
        //SEEN COMMAND
        getProxy().getPluginManager().registerCommand(this, new seen());
        getProxy().getPluginManager().registerListener(this, new seen());


        //ADVANCEDBAN HOOK
        if (hooks.getInstance().hasIsAdvancedBan()) {
            getProxy().getLogger().info(ChatColor.GREEN + "FOUND ADVANCEDBAN! HOOKING IN API");
            try {
                fileconfig.getInstance().createipFile();
                Configuration IpDataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
                fileconfig.getInstance().loadFile(IpDataConfig, ipfile);
            } catch (IOException e) {
                getProxy().getLogger().warning("Unable to load ips. ");
            }
            getProxy().getPluginManager().registerListener(this, new punishmotd());
        } else {
            getProxy().getLogger().info(ChatColor.YELLOW + "ADVANCEDBAN NOT FOUND, DISABLING PUNISHMOTD");
        }
        getProxy().getPluginManager().registerCommand(this, new fernmain());
        run();

    }



    @Override
    public void onDisable(){
        instance = this;
        getLogger().info(ChatColor.AQUA + "DISABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = null;
        seenfile = null;
        configfile = null;
        configp = null;
        instance = null;
        //IpDataConfig = null;
        seendataConfig = null;
        configuration = null;
        config = null;
        ipconfig = null;
        seenconfig = null;
    }

    File getIpFile() {
        return ipfile;
    }

    void setIpFile(File ipFile) {
        ipfile = ipFile;
    }

    //Configuration getIpDataConfig() {
   //     return IpDataConfig;
  //  }

  //  void setIpDataConfig(Configuration ipDataConfig) {
   //     IpDataConfig = ipDataConfig;
  //  }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        bungee.configuration = configuration;
    }

    public ConfigurationProvider getConfigp() {
        return configp;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        bungee.config = config;
    }

    static File getIpfile() {
        return ipfile;
    }

    public Configuration getIpconfig() {
        return ipconfig;
    }




    public BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

    private void run() {
        getProxy().getScheduler().schedule(this, () -> {
            try {
                Configuration load = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            } catch (IOException e) {
                getLogger().warning("unable to reload config");
            }
        }, 3L, TimeUnit.SECONDS);
    }


    public File getSeenfile() {
        return seenfile;
    }

    public Configuration getSeendataConfig() {
        return seendataConfig;
    }

    public Configuration getSeenconfig() {
        return seenconfig;
    }

    @Nonnull
    public static bungee getInstance() {
        return instance;
    }
}
