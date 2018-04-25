package io.github.fernthedev.fcommands.bungeeclass;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
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
    static File ipfile;
    static File seenfile;
    private static Configuration IpDataConfig;
    private static Configuration seendataConfig;
    private static Configuration configuration;
    private static ConfigurationProvider configp;
    private static Configuration config;
    private static Configuration ipconfig;
    private static Configuration seenconfig;
    private static File configfile;
    private static bungee instance;




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
        instance = this;
        getLogger().info("ENABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = new File(getDataFolder(), "ipdata.yml");
        seenfile = new File(getDataFolder(), "seen.yml");
        configfile = new File(getDataFolder(), "config.yml");
        configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
        if (!getDataFolder().exists()) {
            boolean mkdir = getDataFolder().mkdir();
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

        /*try {
            config = configp.load(configfile);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }*/
        createipFile();
        createseenFile();
        File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        File ipfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        File seenfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        getProxy().getPluginManager().registerListener(this, new servermaintenance());
        //SEEN COMMAND
        getProxy().getPluginManager().registerCommand(this, new seen());
        getProxy().getPluginManager().registerListener(this, new seen());
        //ADVANCEDBAN HOOK
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
            seenconfig = configp.load(new File(getDataFolder(), "seen.yml"));
        } catch (IOException e) {
            getLogger().warning("UNABLE TO SAVE SEEN PLAYERS");
        }
        try {
            loadFiles("all");
        } catch (IOException e) {
            getProxy().getLogger().warning("Unable to load config and ip files");
        }
        getProxy().getPluginManager().registerCommand(this,new reloadconfig());
    //configc();

    //getProxy().getPluginManager().registerListener(this, new maintenancemotd());
        run();

    }



    @Override
    public void onDisable(){
        instance = this;
        getLogger().info("DISABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = null;
        seenfile = null;
        configfile = null;
        configp = null;
        instance = null;
        IpDataConfig = null;
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

    Configuration getIpDataConfig() {
        return IpDataConfig;
    }

    void setIpDataConfig(Configuration ipDataConfig) {
        IpDataConfig = ipDataConfig;
    }

    Configuration getConfiguration() {
        return configuration;
    }

    void setConfiguration(Configuration configuration) {
        bungee.configuration = configuration;
    }

    ConfigurationProvider getConfigp() {
        return configp;
    }

    void setConfigp(ConfigurationProvider configp) {
        bungee.configp = configp;
    }

    Configuration getConfig() {
        return config;
    }

    void setConfig(Configuration config) {
        bungee.config = config;
    }

    File getConfigfile() {
        return configfile;
    }

    void setConfigfile(File configfile) {
        bungee.configfile = configfile;
    }

    static File getIpfile() {
        return ipfile;
    }

    public static void setIpfile(File ipfile) {
        bungee.ipfile = ipfile;
    }

    Configuration getIpconfig() {
        return ipconfig;
    }

    void setIpconfig(Configuration ipconfig) {
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createipFile() {
        File IpFile = new File(getDataFolder(), "ipdata.yml");
        if (!IpFile.exists()) {
            try {
                IpFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createseenFile() {
        File SeenFile = new File(getDataFolder(), "seen.yml");
        if (!SeenFile.exists()) {
            try {
                SeenFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }


    @SuppressWarnings("RedundantThrows")
    /**
     * Method for loading config files
     * @param which Which file to load (seen,config,ip,all)
     */
    protected void loadFiles(String which) throws IOException {
        //CONFIG
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
        } else{
            ProxyServer.getInstance().getLogger().warning("Tried to reload config, although file doesn't exist");
        }

        //SEEN
        if (seenfile.exists()) {
            if (which.equals("seen") || which.equals("all")) {
                try {
                    seenconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(seenfile);
                } catch (IOException e) {
                    getProxy().getLogger().warning("failed to load seen config");
                    e.printStackTrace();
                }
                ProxyServer.getInstance().getLogger().info("Seen Config was reloaded  " + which);
            }
        } else{
            ProxyServer.getInstance().getLogger().warning("Tried to reload config, although file doesn't exist");
            ProxyServer.getInstance().getLogger().warning("CREATING SEEN FILE!");
            createseenFile();
        }

        //IP
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

    BaseComponent[] message(String text) {
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

    public void setSeenfile(File seenfile) {
        bungee.seenfile = seenfile;
    }

    public Configuration getSeendataConfig() {
        return seendataConfig;
    }

    public void setSeendataConfig(Configuration seendataConfig) {
        bungee.seendataConfig = seendataConfig;
    }

    public Configuration getSeenconfig() {
        return seenconfig;
    }

    public void setSeenconfig(Configuration seenconfig) {
        bungee.seenconfig = seenconfig;
    }

    public void setInstance(bungee instance) {
        bungee.instance = instance;
    }

    @Nonnull
    public static bungee getInstance() {
        return instance;
    }

}
