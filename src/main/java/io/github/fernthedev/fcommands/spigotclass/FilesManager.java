package io.github.fernthedev.fcommands.spigotclass;

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
    FileConfiguration config = spigot.getInstance().getConfig();
    private File configfile = new File(spigot.getInstance().getDataFolder(), "config.yml");
    /**
     *
     * @param which Which config to reload, such as (config,all)
     */
    public void reloadConfig(String which) throws IOException, InvalidConfigurationException {
        if(which.equals("all") || which.equals("config")) {
            if(configfile.exists()) {
                config.load(new File(spigot.getInstance().getDataFolder(),"config.yml"));
            }else{
                //createFile();
                FilesManager.getInstance().setDefault();
            }
        }
    }

    public void createFile(){
        File file = new File(spigot.getInstance().getDataFolder(), "players.yml");
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
        //FileConfiguration config = spigot.getInstance().getConfig();
        spigot.getInstance().getConfig().set("nodmgepearl",false);
        config.set("NoIgDoorFarm",false);
        config.set("tpbow",false);
        config.set("BungeeNCP",true);
        config.set("BedFire",false);
        config.set("ItemBurn",false);
        config.set("Skylands",false);
        config.set("NameColor",false);
        spigot.getInstance().saveConfig();
    }

    private void createConfig() {
        try {
            if (!spigot.getInstance().getDataFolder().exists()) {
                spigot.getInstance().getDataFolder().mkdirs();
            }
            File file = new File(spigot.getInstance().getDataFolder(), "config.yml");
            if (!file.exists()) {
                spigot.getInstance().getLogger().info("Config.yml not found, creating!");
                spigot.getInstance().saveDefaultConfig();
            } else {
                spigot.getInstance().getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
