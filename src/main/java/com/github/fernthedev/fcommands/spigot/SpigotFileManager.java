package com.github.fernthedev.fcommands.spigot;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.config.gson.GsonConfig;
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

    private final Config<NewSpigotConfig> newSpigotConfigConfig;

    public Config<NewSpigotConfig> getNewSpigotConfigConfig() {
        return newSpigotConfigConfig;
    }

    @Inject
    public SpigotFileManager(FileConfiguration config, Plugin plugin) {
        this.config = config;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        this.plugin = plugin;
        try {
            newSpigotConfigConfig = new GsonConfig<>(new NewSpigotConfig(), new File(plugin.getDataFolder(), "newconfig.json"));
            newSpigotConfigConfig.load();
            newSpigotConfigConfig.save();
        } catch (ConfigLoadException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public <T> T getValue(String val, T defval) {
        if(val != null && defval != null) {
            if (config.get(val) == null) {
                config.set(val, defval);
            }
            return (T) config.get(val);
        }
        return null;
    }

    @Deprecated
    public String getValue(String val, String defval) {
        if(val != null && defval != null) {
            if (config.get(val) == null) {
                config.set(val, defval);
            }
            return config.getString(val);
        }
        return null;
    }

    @Deprecated
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

            newSpigotConfigConfig.syncLoad();
            newSpigotConfigConfig.syncSave();
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
