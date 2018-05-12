package io.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileManager {

    private static File configfile;
    private static File ipfile;
    private static File seenfile;
    private static FileManager thisinstance;
    private static Configuration config;
    private static Configuration ipconfig;
    private static Configuration seenconfig;

    /**
     * The constructor for setting the instance;
     */
    public FileManager() {
        thisinstance = this;
        ipfile = new File(FernCommands.getInstance().getDataFolder(), "ipdata.yml");
        seenfile = new File(FernCommands.getInstance().getDataFolder(), "seen.yml");
        configfile = new File(FernCommands.getInstance().getDataFolder(), "config.yml");
        ConfigurationProvider configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
    }



    /**
      Method for loading config files
      @param which Which file to load (seen,config,ip,all)
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
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
            } catch (IOException e) {
                FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load config");
                e.printStackTrace();
            }
            if(!silent)
            FernCommands.getInstance().getLogger().info("Config was reloaded  " + which);
        }else if(goconfig){
            FernCommands.getInstance().getLogger().warning("Tried to reload config, although file doesn't exist");
            FernCommands.getInstance().getLogger().warning("Creating config");
            createConfig();
        }


        //SEEN
        boolean goseen = which.equalsIgnoreCase("seen") || which.equalsIgnoreCase("all");
        if (seenfile.exists() && goseen) {
            try {
                seenconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(seenfile);
            } catch (IOException e) {
                FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load seen config");
                e.printStackTrace();
            }
            if(!silent)
            FernCommands.getInstance().getLogger().info("Seen Config was reloaded  " + which);
        } else if(goseen) {
            FernCommands.getInstance().getLogger().warning("Tried to reload seen config, although file doesn't exist");
            FernCommands.getInstance().getLogger().warning("CREATING SEEN FILE!");
            createseenFile();
        }

        //IP
        boolean goip = which.equalsIgnoreCase("ip") || which.equalsIgnoreCase("all");
        if (hooks.getInstance().hasIsAdvancedBan()) {
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
     * Method for creating seen config
     */
    public void createseenFile() {
        File SeenFile = new File(FernCommands.getInstance().getDataFolder(), "seen.yml");
        if (!SeenFile.exists()) {
            try {
                SeenFile.createNewFile();
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
        File IpFile = new File(FernCommands.getInstance().getDataFolder(), "ipdata.yml");
        if (!IpFile.exists()) {
            try {
                IpFile.createNewFile();
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
            try (InputStream in = FernCommands.getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // config.getKeys().add("Motd:");
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

    public static void onDisable() {
        config = null;
        configfile = null;
        ipfile = null;
        seenfile = null;
        thisinstance = null;
        ipconfig = null;
        seenconfig = null;
    }
}
