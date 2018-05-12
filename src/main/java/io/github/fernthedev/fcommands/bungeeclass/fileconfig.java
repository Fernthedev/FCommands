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
public class fileconfig {

    private static File configfile;
    private static File ipfile;
    private static File seenfile;
    private static ConfigurationProvider configp;
    private static fileconfig thisinstance;

    /**
     * The constructor for setting the instance;
     */
    public fileconfig() {
        thisinstance = this;
        ipfile = new File(bungee.getInstance().getDataFolder(), "ipdata.yml");
        seenfile = new File(bungee.getInstance().getDataFolder(), "seen.yml");
        configfile = new File(bungee.getInstance().getDataFolder(), "config.yml");
        configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
        try {
            loadFiles("all");
        } catch (IOException e) {
            bungee.getInstance().getProxy().getLogger().warning("Unable to load config and ip files");
        }
    }



    /**
      Method for loading config files
      @param which Which file to load (seen,config,ip,all)
     */
    @SuppressWarnings("RedundantThrows")
    public void loadFiles(String which) throws IOException {
        //CHECK IF PLUGIN FOLDER EXISTS
        if (!bungee.getInstance().getDataFolder().exists()) {
            boolean mkdir = bungee.getInstance().getDataFolder().mkdir();
        }


        boolean goconfig = which.equalsIgnoreCase("config") || which.equalsIgnoreCase("all");
        //CONFIG
        if (goconfig && configfile.exists()) {
            try {
                Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
            } catch (IOException e) {
                bungee.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load config");
                e.printStackTrace();
            }
            bungee.getInstance().getLogger().info("Config was reloaded  " + which);
        }else if(goconfig){
            bungee.getInstance().getLogger().warning("Tried to reload config, although file doesn't exist");
            bungee.getInstance().getLogger().warning("Creating config");
            createConfig();
        }


        //SEEN
        boolean goseen = which.equalsIgnoreCase("seen") || which.equalsIgnoreCase("all");
        if (seenfile.exists() && goseen) {
            try {
                Configuration seenconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(seenfile);
            } catch (IOException e) {
                bungee.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load seen config");
                e.printStackTrace();
            }
            bungee.getInstance().getLogger().info("Seen Config was reloaded  " + which);
        } else if(goseen) {
            bungee.getInstance().getLogger().warning("Tried to reload seen config, although file doesn't exist");
            bungee.getInstance().getLogger().warning("CREATING SEEN FILE!");
            createseenFile();
        }

        //IP
        boolean goip = which.equalsIgnoreCase("ip") || which.equalsIgnoreCase("all");
        if (hooks.getInstance().hasIsAdvancedBan()) {
            if (ipfile.exists() && (which.equals("ip") || which.equals("all"))) {
                try {
                    Configuration ipconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
                } catch (IOException e) {
                    bungee.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load ips");
                    e.printStackTrace();
                }
                bungee.getInstance().getLogger().info("Ips was reloaded  " + which);
            } else if(goip) {
                bungee.getInstance().getLogger().warning("Tried to reload ips, although file doesn't exist");
                bungee.getInstance().getLogger().warning("CREATING IP FILE!");
                createipFile();
            }
        } else {
            bungee.getInstance().getLogger().warning("Tried to reload/create ips although advancedban isn't loaded, so why load the file?");
        }

    }


    /**
     * Method for reloading files seperately instead of preset.
     * @param whichconfig The configuration to use to change setting
     * @param which What file is associated with the configuration
     */
    public void loadFile(Configuration whichconfig,File which) {
        try {
            whichconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(which);
        } catch (IOException e) {
            bungee.getInstance().getLogger().warning("unable to load file, saving anyways");
        }
        bungee.getInstance().getLogger().info("Loaded " + which + " with configuration " + whichconfig);
    }


    /**
     * Method for creating seen config
     */
    public void createseenFile() {
        File SeenFile = new File(bungee.getInstance().getDataFolder(), "seen.yml");
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
        File IpFile = new File(bungee.getInstance().getDataFolder(), "ipdata.yml");
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
            bungee.getInstance().getLogger().warning("unable to load file, saving anyways");
        }*/
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(whichconfig,which);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static fileconfig getInstance() {
        return thisinstance;
    }



    public void createConfig() {
        if (!bungee.getInstance().getDataFolder().exists()) {
            boolean mkdir = bungee.getInstance().getDataFolder().mkdir();
        }
        File file = new File(bungee.getInstance().getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = bungee.getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // config.getKeys().add("Motd:");
        }
    }
}
