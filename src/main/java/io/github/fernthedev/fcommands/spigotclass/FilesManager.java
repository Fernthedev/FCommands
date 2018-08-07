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


    FileConfiguration config = FernCommands.getInstance().getConfig();
    private File configfile = new File(FernCommands.getInstance().getDataFolder(), "config.yml");



    /**
     *
     * @param which Which config to reload, such as (config,all)
     */
    public void reloadConfig(String which) throws IOException, InvalidConfigurationException {
        if(which.equals("all") || which.equals("config")) {
            if(configfile.exists()) {
                config.load(new File(FernCommands.getInstance().getDataFolder(),"config.yml"));
            }else{
                //createFile();
                FilesManager.getInstance().setDefault();
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

    public void setDefault() {
        ourInstance = this;
        //FileConfiguration config = FernCommands.getInstance().getConfig();
        FernCommands.getInstance().getConfig().set("nodmgepearl",false);
        config.set("NoIgDoorFarm",false);
        config.set("tpbow",false);
        config.set("BungeeNCP",true);
        config.set("BedFire",false);
        config.set("ItemBurn",false);
        config.set("Skylands",false);
        config.set("NameColor",false);
        FernCommands.getInstance().saveConfig();
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
