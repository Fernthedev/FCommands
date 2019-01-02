package com.github.fernthedev.fcommands.spigotclass;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FilesManager {

    private static FilesManager ourInstance = new FilesManager();

    public static FilesManager getInstance() {
        return ourInstance;
    }

    private FilesManager() {
        ourInstance = this;
    }


    static FileConfiguration config = FernCommands.getInstance().getConfig();
    private File configFile = new File(FernCommands.getInstance().getDataFolder(), "config.yml");

    static FileConfiguration chestConfig = FernCommands.getInstance().getConfig();
    private static File chestConfigFile = new File(FernCommands.getInstance().getDataFolder(), "chest.yml");


    public static FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    public static FileConfiguration getChestConfig() {
        return chestConfig;
    }

    public static File getChestConfigFile() {
        return chestConfigFile;
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


    /**
     *
     * @param which Which config to reload, such as (config,all)
     */
    public void reloadConfig(String which) throws IOException, InvalidConfigurationException {
        if(which.equals("all") || which.equals("config")) {
            if(configFile.exists()) {
                config.load(new File(FernCommands.getInstance().getDataFolder(),"config.yml"));
                setDefault();
            }else{
                //createFile();
                FilesManager.getInstance().setDefault();
            }
        }

        if(which.equals("all") || which.equals("chest")) {
            if(chestConfigFile.exists()) {
                chestConfig.load(chestConfigFile);
            }
        }
    }

    public void createFile(){
        File file = new File(FernCommands.getInstance().getDataFolder(), "players.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createChestFile(){
        File file = chestConfigFile;
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDefault() {
        ourInstance = this;
        //FileConfiguration config = FernCommands.getInstance().getConfig();
        if(config.get("nodmgepearl") == null ) {
            FernCommands.getInstance().getConfig().set("nodmgepearl", false);
        }
        
        
        setCheck("NoIgDoorFarm",false);
        setCheck("tpbow",false);
        setCheck("BungeeNCP",true);
        setCheck("BedFire",false);
        setCheck("ItemBurn",false);
        setCheck("Skylands",false);
        setCheck("NameColor",false);
        setCheck("NoAIonSpawn",false);
        setCheck("AddShop",false);
        
        FernCommands.getInstance().saveConfig();
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
            if (!FernCommands.getInstance().getDataFolder().exists()) {
                FernCommands.getInstance().getDataFolder().mkdirs();
            }
            File file = new File(FernCommands.getInstance().getDataFolder(), "config.yml");
            if (!file.exists()) {
                FernCommands.getInstance().getLogger().info("Config.yml not found, creating!");
                FernCommands.getInstance().saveDefaultConfig();
            } else {
                FernCommands.getInstance().getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
