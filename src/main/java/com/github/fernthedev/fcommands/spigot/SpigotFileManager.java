package com.github.fernthedev.fcommands.spigot;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;


@Getter
@Singleton
public class SpigotFileManager {

    private final FileConfiguration config;
    private final File configFile;

    private final Plugin plugin;

    @Inject
    public SpigotFileManager(FileConfiguration config, Plugin plugin) {
        this.config = config;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        this.plugin = plugin;
    }

    public <T> T getValue(String val, T defval) {
        if(val != null && defval != null) {
            if (config.get(val) == null) {
                config.set(val, defval);
            }
            return (T) config.get(val);
        }
        return null;
    }

    public String getValue(String val, String defval) {
        if(val != null && defval != null) {
            if (config.get(val) == null) {
                config.set(val, defval);
            }
            return config.getString(val);
        }
        return null;
    }

    public boolean getValue(String val, boolean defval) {
        if(val != null) {
            if (config.get(val) == null) {
                config.set(val, defval);
            }
            return config.getBoolean(val);
        }
        return false;
    }

    /**
     *
     * @param which Which config to reload, such as (config,all)
     */
    public void reloadConfig(String which) throws IOException, InvalidConfigurationException {
        if(which.equals("all") || which.equals("config")) {
            if(configFile.exists()) {
                config.load(configFile);
                setDefault();
            }else{
                //createFile();
                setDefault();
            }
        }
    }




    public void setDefault() {
        setCheck("nodmgpearl", false);
        
        setCheck("NoIgDoorFarm",false);
        setCheck("tpbow",false);
        setCheck("BungeeNCP",true);
        setCheck("BedFire",false);
        setCheck("ItemBurn",false);
        setCheck("Skylands",false);
        setCheck("NameColor",false);
        setCheck("NoAIonSpawn",false);
        setCheck("RandomKick", false);
        setCheck("RandomKickMessageCountdown", "&c&lRemoving trash in %count%");
        setCheck("RandomKickMessage", "&a&lRemoved %player% which is trash");
        setCheck("RandomKickCountTimeSeconds", 5 * 60);

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setCheck(String value,Object def) {
        if(value != null && def != null) {
            if (config.get(value) == null) {
                config.set(value, def);
            }
        }
    }

    private void createConfig() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            File file = new File(plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                plugin.getLogger().info("Config.yml not found, creating!");
                plugin.saveDefaultConfig();
            } else {
                plugin.getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public int getValue(String val, int defval) {

        if (val != null) {
            if (config.get(val) == null) {
                config.set(val, defval);
            }
            return config.getInt(val);
        }
        return 0;
    }
}
