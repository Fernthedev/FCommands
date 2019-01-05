package com.github.fernthedev.fcommands.bungeeclass;


import com.github.fernthedev.fcommands.bungeeclass.files.ConfigManager;
import com.github.fernthedev.fcommands.bungeeclass.files.ConfigValues;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileManager {

    private static File configfile;
    private static File ipfile;
    private static File seenfile;
    private static File deleteipfile;
    private static FileManager thisinstance;
    private static Configuration config;
    private static Configuration ipconfig;
    private static Configuration seenconfig;
    private static Configuration deleteipconfig;

    private static ConfigManager configManager;

    public File getConfigfile() {
        return configfile;
    }

    /**
     * The constructor for setting the instance;
     */
    public FileManager() {
        registerVars();
    }

    private void registerVars() {
        thisinstance = this;
        deleteipfile = new File(FernCommands.getInstance().getDataFolder(),"ipdelete.yml");
        ipfile = new File(FernCommands.getInstance().getDataFolder(), "ipdata.yml");
        seenfile = new File(FernCommands.getInstance().getDataFolder(), "seen.yml");
        configfile = new File(FernCommands.getInstance().getDataFolder(), "config.json");
        ConfigurationProvider configp = ConfigurationProvider.getProvider(YamlConfiguration.class);

        configManager = new ConfigManager();
    }

    /**
     * This returns the value of the path, if there is none it returns the default value
     * @param path The value path
     * @param defval The default value if none is defined
     * @return The path's value, if there is none it returns default.
     */
    public String getValue(String path,Object defval) {
        try {
            loadFiles(WhichFile.CONFIG,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(path != null && defval != null) {
            if(config.get(path) == null) {
                config.set(path,defval);
                saveFiles(config,configfile);
            }

            return config.getString(path);
        }
        return null;
    }



    /**
      Method for loading config files
      @param which Which file to load (Seen,config,ip,all)
     */
    @SuppressWarnings("RedundantThrows")
    public void loadFiles(String which,boolean silent) throws IOException {
        //CHECK IF PLUGIN FOLDER EXISTS
        if (!FernCommands.getInstance().getDataFolder().exists()) {
            FernCommands.getInstance().getDataFolder().mkdir();
        }


        boolean goconfig = which.equalsIgnoreCase("config") || which.equalsIgnoreCase("all");
        //CONFIG
        if (goconfig && configfile.exists()) {
            configManager.load();
            /*
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
            } catch (IOException e) {
                FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load config");
                e.printStackTrace();
            }
            if(!silent)
            FernCommands.getInstance().getLogger().info("Config was reloaded  " + which);*/
        }else if(goconfig){
            FernCommands.getInstance().getLogger().warning("Tried to reload config, although file doesn't exist");
            FernCommands.getInstance().getLogger().warning("Creating config");
            createConfig();
        }


        //SEEN
        boolean goseen = which.equalsIgnoreCase("Seen") || which.equalsIgnoreCase("all");
        if (seenfile.exists() && goseen) {
            try {
                seenconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(seenfile);
            } catch(org.yaml.snakeyaml.scanner.ScannerException e) {
                FernCommands.getInstance().getProxy().getLogger().info("Unable to load seenconfig. Error: " + e.getProblem());

            } catch (IOException e) {
                FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load Seen config");
                e.printStackTrace();
            }
            if(!silent)
            FernCommands.getInstance().getLogger().info("Seen Config was reloaded  " + which);
        } else if(goseen) {
            FernCommands.getInstance().getLogger().warning("Tried to reload Seen config, although file doesn't exist");
            FernCommands.getInstance().getLogger().warning("CREATING SEEN FILE!");
            createseenFile();
        }

        //IP
        boolean goip = which.equalsIgnoreCase("ip") || which.equalsIgnoreCase("all");
        if (HookManager.getInstance().hasAdvancedBan()) {
            if (ipfile.exists() && (which.equals("ip") || which.equals("all"))) {
                try {
                    ipconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
                } catch (IOException e) {
                    FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load ips");
                    e.printStackTrace();
                }
                if(!silent)
                FernCommands.getInstance().getLogger().info("Ips was reloaded  " + which);
            } else if(goip) {
                FernCommands.getInstance().getLogger().warning("Tried to reload ips, although file doesn't exist");
                FernCommands.getInstance().getLogger().warning("CREATING IP FILE!");
                createipFile();
            }
        } else {
            FernCommands.getInstance().getLogger().warning("Tried to reload/create ips although advancedban isn't loaded, so why load the file?");
        }

        //DeleteIP
        //IP
        boolean goipdelete = which.equalsIgnoreCase("ipdelete") || which.equalsIgnoreCase("all");
        if (HookManager.getInstance().hasAdvancedBan()) {
            if (deleteipfile.exists() && goipdelete) {
                try {
                    deleteipconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(deleteipfile);
                } catch (IOException e) {
                    FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load deleteips");
                    e.printStackTrace();
                }
                if(!silent)
                    FernCommands.getInstance().getLogger().info("deleteIps was reloaded  " + which);
            } else if(goipdelete) {
                FernCommands.getInstance().getLogger().warning("Tried to reload deleteips, although file doesn't exist");
                FernCommands.getInstance().getLogger().warning("CREATING deleteIP FILE!");
                createDeleteIp();
            }
        } else {
            FernCommands.getInstance().getLogger().warning("Tried to reload/create deleteips although advancedban isn't loaded, so why load the file?");
        }
    }

    public void loadFiles(WhichFile file,boolean silent) throws IOException {
        loadFiles(file.toString(),silent);
    }


    /**
     * Method for reloading files seperately instead of preset.
     * //@param whichconfig The configuration to use to change setting
     * @param which What file is associated with the configuration
     */
    public void loadFile(File which) throws IOException {
        Configuration whichconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(which);
        FernCommands.getInstance().getLogger().info("Loaded " + which + " with configuration " + whichconfig);
    }


    /**
     * Method for creating Seen config
     */
    public void createseenFile() {
        File seenFile = new File(FernCommands.getInstance().getDataFolder(), "Seen.yml");
        if (!seenFile.exists()) {
            try {
                seenFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }

    /**
     * Method for creating ipFile
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createipFile() {
        File ipFile = new File(FernCommands.getInstance().getDataFolder(), "ipdata.yml");
        if (!ipFile.exists()) {
            try {
                ipFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }


    /**
     * The method I will for now on use to save files
     * @param whichconfig The configuration that should be used for saving file
     * @param which The location of the config file
     */
    public void saveFiles(Configuration whichconfig,File which) {
        /*try {
            confige = ConfigurationProvider.getProvider(YamlConfiguration.class).load(which);
        } catch (IOException e) {
            FernCommands.getInstance().getLogger().warning("unable to load file, saving anyways");
        }*/
        try {
            loadFile(which);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(whichconfig,which);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static FileManager getInstance() {
        return thisinstance;
    }



    public void createConfig() {
        if (!FernCommands.getInstance().getDataFolder().exists()) {
            FernCommands.getInstance().getDataFolder().mkdir();
        }
        File file = new File(FernCommands.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            configManager.load();
        }
    }

    /**
     * Method for creating ipFile
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createDeleteIp() {
        File IpFile = new File(FernCommands.getInstance().getDataFolder(), "ipdelete.yml");
        if (!IpFile.exists()) {
            try {
                IpFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getIpconfig() {
        return ipconfig;
    }

    public Configuration getSeenconfig() {
        return seenconfig;
    }

    public Configuration getDeleteipconfig() {
        return deleteipconfig;
    }

    public static void onDisable() {
        config = null;
        configfile = null;
        ipfile = null;
        seenfile = null;
        deleteipfile = null;
        deleteipconfig = null;
        thisinstance = null;
        ipconfig = null;
        seenconfig = null;
    }

    public enum WhichFile {
        IP("ip")
        ,CONFIG("config"),SEEN("Seen"),DELETEIP("ipdelete"),ALL("all");

        private String value;

        WhichFile(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static ConfigValues getConfigValues() {
        return configManager.getConfigValues();
    }
}
