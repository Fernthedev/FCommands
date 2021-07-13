package com.github.fernthedev.fcommands.proxy;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import com.github.fernthedev.config.gson.GsonConfig;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.proxy.data.IPDeleteValues;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fcommands.proxy.data.SeenValues;
import com.github.fernthedev.fernapi.universal.Universal;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileManager {

    private static FileManager thisinstance;

//    private static ConfigManager configManager;

    @Getter
    private static Config<ConfigValues> configManager;

    @Getter
    private static Config<SeenValues> seenConfig;

    @Getter
    private static Config<IPSaveValues> ipConfig;

    @Getter
    private static Config<IPDeleteValues> deleteIPConfig;


    /**
     * The constructor for setting the instance;
     */
    public FileManager() {
        registerVars();
    }

    private void registerVars() {
        thisinstance = this;

        try {
            configManager = new GsonConfig<>(new ConfigValues(), new File(Universal.getMethods().getDataFolder(), "config.json"));
            configManager.load();

            deleteIPConfig = new GsonConfig<>(new IPDeleteValues(), new File(Universal.getMethods().getDataFolder(), "ipdelete.json"));
            deleteIPConfig.load();

            ipConfig = new GsonConfig<>(new IPSaveValues(), new File(Universal.getMethods().getDataFolder(), "ipdata.json"));
            ipConfig.load();

            seenConfig = new GsonConfig<>(new SeenValues(), new File(Universal.getMethods().getDataFolder(), "seen.json"));
            seenConfig.load();

        } catch (ConfigLoadException e) {
            throw new IllegalStateException(e);
        }
//        configManager = new ConfigManager();
    }


    /**
     * Method for loading config files
     *
     * @param which Which file to load (Seen,config,ip,all)
     */
    @SuppressWarnings("RedundantThrows")
    @Deprecated
    @Synchronized
    public void loadFiles(String which, boolean silent) throws IOException {
        new IllegalAccessError("This is deprecated. Just an error message").printStackTrace();
        //CHECK IF PLUGIN FOLDER EXISTS
        if (!Universal.getMethods().getDataFolder().exists()) {
            Universal.getMethods().getDataFolder().mkdir();
        }


        boolean goConfig = which.equalsIgnoreCase("config") || which.equalsIgnoreCase("all");
        //CONFIG
        if (goConfig) {
            FileManager.configLoad(configManager);
            /*
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
            } catch (IOException e) {
                FernCommands.getInstance().getProxy().getLogger().warning(ChatColor.RED + "failed to load config");
                e.printStackTrace();
            }
            if(!silent)
            Universal.getMethods().getLogger().info("Config was reloaded  " + which);*/
        }


        //SEEN
        boolean goSeen = which.equalsIgnoreCase("Seen") || which.equalsIgnoreCase("all");
        if (goSeen) {
            FileManager.configLoad(seenConfig);

            if (!silent)
                Universal.getMethods().getAbstractLogger().info("Seen Config was reloaded  " + which);
        }

        //IP
        boolean goIP = which.equalsIgnoreCase("ip") || which.equalsIgnoreCase("all");

        if (goIP) {
            FileManager.configLoad(ipConfig);

            if (!silent)
                Universal.getMethods().getAbstractLogger().info("Ips was reloaded  " + which);
        }


        //DeleteIP
        //IP
        boolean goIPDelete = which.equalsIgnoreCase("ipdelete") || which.equalsIgnoreCase("all");
        if (goIPDelete) {
            FileManager.configLoad(deleteIPConfig);

            if (!silent)
                Universal.getMethods().getAbstractLogger().info("deleteIps was reloaded  " + which);
        }
    }

    @Deprecated
    @Synchronized
    public void loadFiles(WhichFile file, boolean silent) throws IOException {
        loadFiles(file.toString(), silent);
    }

    public static FileManager getInstance() {
        return thisinstance;
    }

    public enum WhichFile {
        IP("ip"),
        CONFIG("config"),
        SEEN("Seen"),
        DELETEIP("ipdelete"),
        ALL("all");

        private String value;

        WhichFile(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Just used to synchronise
     */
    @SneakyThrows
    public static void configLoad(Config<?> config) {
        config.syncLoad();
    }

    /**
     * Just used to synchronise
     */
    @SneakyThrows
    public static void configSave(Config<?> config) {
        config.syncSave();
    }

    /**
     * Synchronise config instances
     * @param config
     * @return
     */
    @Synchronized
    public static Config getConfig(Config<?> config) {
        return config;
    }

    public static ConfigValues getConfigValues() {
        return configManager.getConfigData();
    }
}
