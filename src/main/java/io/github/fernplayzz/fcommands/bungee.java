package io.github.fernplayzz.fcommands;


import io.github.fernplayzz.fcommands.bungeeclass.punishmotd;
import io.github.fernplayzz.fcommands.bungeeclass.reloadconfig;
import io.github.fernplayzz.fcommands.bungeeclass.servermaintenance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class bungee extends Plugin {

    public static File pluginFolder;
    public static File ipfile;
    public static Configuration IpDataConfig;
    public static Configuration configuration;
    public static ConfigurationProvider configp;
    public static Configuration config;
    public static Configuration ipconfig;
    public static File configfile;
    //public static Configuration config;
   // @Override
   // public void onEnable() {
     //   getLogger().info("BUNGEECORD IS NOT SUPPORTED YET!");
       // getLogger().info("DISABLING PLUGIN!");
       // getProxy().getPluginManager().unregisterListeners(this);
       // getProxy().getPluginManager().unregisterCommands(this);
        //this.onDisable();
    //}*/
    @Override
   public void onEnable() {
        getLogger().info("ENABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = new File(getDataFolder(), "ipdata.yml");
        configfile = new File(getDataFolder(), "config.yml");
        configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
           // config.getKeys().add("Motd:");
        }

        /*try {
            config = configp.load(configfile);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }*/
        createipFile();
        File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        File ipfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        getProxy().getPluginManager().registerListener(this, new servermaintenance());
    if(getProxy().getPluginManager().getPlugin("AdvancedBan") !=null) {
        getProxy().getLogger().info("FOUND ADVANCEDBAN! HOOKING IN API");
        try {
            /*if (!ipfile.exists()) {
                try {
                    ipfile.createNewFile();
                } catch (IOException e) {
                    getProxy().getLogger().warning("Unable to create ipdata file");
                }
            }*/
            createipFile();
            IpDataConfig = configp.load(new File(getDataFolder(), "ipdata.yml"));
        } catch (IOException e) {
            getProxy().getLogger().warning("Unable to load ips. ");
        }
        getProxy().getPluginManager().registerListener(this, new punishmotd());
        }else{
        getProxy().getLogger().info("ADVANCEDBAN NOT FOUND, DISABLING PUNISHMOTD");
    }
        try {
            loadFiles("all");
        } catch (IOException e) {
            getProxy().getLogger().warning("Unable to load config and ip files");
        }
        getProxy().getPluginManager().registerCommand(this,new reloadconfig());
    //configc();

    //getProxy().getPluginManager().registerListener(this, new maintenancemotd());
        getProxy().getScheduler().schedule(this, new Runnable(){
            @Override
            public void run() {
                try {
                    Configuration load = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
                } catch (IOException e) {
                    getLogger().warning("unable to reload config");
                }
            }
        }, 3L, TimeUnit.SECONDS);
    }



    @Override
    public void onDisable(){
        Logger log = getProxy().getLogger();
        /*try {
            configp.save(config, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            log.warning("UNABLE TO SAVE CONFIG");
        }
        try {
            ipconfig = configp.load(ipfile, ipconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            configp.save(ipconfig, ipfile);
        } catch (IOException e) {
            log.warning("UNABLE TO SAVE IPS");
        }*/
    }

    public File getIpFile() {
        return ipfile;
    }

    public void setIpFile(File ipFile) {
        ipfile = ipFile;
    }

    public Configuration getIpDataConfig() {
        return IpDataConfig;
    }

    public void setIpDataConfig(Configuration ipDataConfig) {
        IpDataConfig = ipDataConfig;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        bungee.configuration = configuration;
    }

    public ConfigurationProvider getConfigp() {
        return configp;
    }

    public void setConfigp(ConfigurationProvider configp) {
        bungee.configp = configp;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        bungee.config = config;
    }

    public File getConfigfile() {
        return configfile;
    }

    public void setConfigfile(File configfile) {
        bungee.configfile = configfile;
    }

    public static File getIpfile() {
        return ipfile;
    }

    public static void setIpfile(File ipfile) {
        bungee.ipfile = ipfile;
    }

    public Configuration getIpconfig() {
        return ipconfig;
    }

    public void setIpconfig(Configuration ipconfig) {
        bungee.ipconfig = ipconfig;
    }
    //public Configuration getConfig() {
    //    return config;
    //}

    //public void setConfig(Configuration config) {
    //    bungee.config = config;
   // }

    /*public void loadIps() {
        //IpDataConfig = configp.load(new File(getDataFolder(), "ipdata.yml"));
        try {
            //IpDataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "ipdata.yml"));
            IpDataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(IpFile);
        } catch (IOException e) {
            getProxy().getLogger().warning("unable to load IPs");
        }
    }*/

    public void SaveIp(Configuration IpDataConfige) {
        try {
            //configp.save(IpDataConfig, new File(getDataFolder(), "ipdata.yml"));
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(IpDataConfige, new File(getDataFolder(), "ipdata.yml"));
        } catch (IOException e) {
            //
            getProxy().getLogger().warning("unable to save");
            e.printStackTrace();
        }
    }

    public void createipFile() {
        File IpFile = new File(getDataFolder(), "ipdata.yml");
        if (!IpFile.exists()) {
            try {
                IpFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }

    public void loadFiles(String which) throws IOException {
        if (configfile.exists()) {
            if (which.equals("config") || which.equals("all")) {
                try {
                    config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
                } catch (IOException e) {
                    getProxy().getLogger().warning("failed to load config");
                    e.printStackTrace();
                }
                ProxyServer.getInstance().getLogger().info("Config was reloaded  " + which);
            }
        } else if (new File(getDataFolder(), "config.yml").exists()) {
            ProxyServer.getInstance().getLogger().warning("Tried to reload config, although file doesn't exist");
        }
        if (ipfile.exists()) {
            if (which.equals("ip") || which.equals("all")) {

                try {
                    ipconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
                } catch (IOException e) {
                    getProxy().getLogger().warning("failed to load ips");
                    e.printStackTrace();
                }
                ProxyServer.getInstance().getLogger().info("Ips was reloaded  " + which);
            }
        }else{
            ProxyServer.getInstance().getLogger().warning("Tried to reload ips, although file doesn't exist");
        }
    }

    public BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

}
