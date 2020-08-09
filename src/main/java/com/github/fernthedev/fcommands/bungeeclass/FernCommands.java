package com.github.fernthedev.fcommands.bungeeclass;


import com.github.fernthedev.fcommands.bungeeclass.commands.BungeePluginList;
import com.github.fernthedev.fcommands.bungeeclass.commands.ip.AltsBan;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.commands.*;
import com.github.fernthedev.fcommands.proxy.commands.ip.MainIP;
import com.github.fernthedev.fcommands.proxy.commands.ip.ShowAlts;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.universal.*;
import com.github.fernthedev.fcommands.universal.commands.NameHistory;
import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("unused")
public class FernCommands extends FernBungeeAPI {

    //public static File pluginFolder;
   // private static Configuration IpDataConfig;
    //private static Configuration configuration;
    //private static ConfigurationProvider configp;

    //private static File configfile;
    private static FernCommands instance;

    private Universal universal;

    @Getter
    private static DBManager databaseManager;

    private static List<Runnable> runnableList = new ArrayList<>();

    public static List<Runnable> getRunnables() {
        return runnableList;
    }

    @Getter
    private static HookManager hookManager;

    @Getter
    private static FileManager fileManager;


    @Override
   public void onEnable() {
        super.onEnable();

        instance = this;
        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR BUNGEECORD");

        //configfile = new File(getDataFolder(), "config.yml");
        //configp = ConfigurationProvider.getProvider(YamlConfiguration.class);

        if (!getDataFolder().exists()) {
            boolean mkdir = getDataFolder().mkdir();
            getLogger().info(mkdir + " folder make");
        }
        hookManager = new HookManager();
        fileManager = new FileManager();


        getProxy().getPluginManager().registerCommand(this, new BungeePluginList());
        getProxy().getPluginManager().registerListener(this, new Events());

//        File file = new File(getDataFolder(), "config.yml");
//        if (!file.exists()) {
//
//            try (InputStream in = getResourceAsStream("config.yml")) {
//                Files.copy(in, file.toPath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
        ConfigValues configValues = FileManager.getConfigManager().getConfigData();
        DatabaseAuthInfo databaseAuthInfo = configValues.getDatabaseAuthInfo();
        if (configValues.isDatabaseConnect()) {
            getLogger().info("Using database info: database:" + databaseAuthInfo.getDatabase() + " host: " + databaseAuthInfo.getUrlHost() + " port: " + databaseAuthInfo.getPort() + " user: " + databaseAuthInfo.getUsername());

            databaseManager = new DBManager(databaseAuthInfo.getUsername(), databaseAuthInfo.getPassword(), databaseAuthInfo.getPort(), databaseAuthInfo.getUrlHost(), databaseAuthInfo.getDatabase());
            UniversalMysql.setDatabaseManager(databaseManager);
        }
//            getLogger().info("Attempting to connect to MySQL");
////            DatabaseHandler.setup();
//            getLogger().info("Mysql connection attempted.");
//            connection = DatabaseHandler.getConnection();


        //CREATING SEEN FILE
//        LegacyFileManager.getSeenConfig().load();

        //File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        //File ipfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        //File seenfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");


        ServerMaintenanceMOTD maintenanceMOTD = new ServerMaintenanceMOTD();
        maintenanceMOTD.setupTask();
        getProxy().getPluginManager().registerListener(this, maintenanceMOTD);
        //SEEN COMMAND
        if(FileManager.getConfigValues().isAllowSeenCommand()) {
            Universal.getCommandHandler().registerCommand(new Seen());
//            getProxy().getPluginManager().registerCommand(this, new Seen());
//            getProxy().getPluginManager().registerListener(this, new Seen());
        }

//        getProxy().getPluginManager().registerCommand(this, new PreferenceCommand("fernc.preference", "preference"));

        Universal.getCommandHandler().registerCommand(new PreferenceCommand());

//        try {
//        LegacyFileManager.getIpConfig().load();
//            LegacyFileManager.getInstance().createipFile();
//            //Configuration IpDataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
//            //LegacyFileManager.getInstance().loadFile(ipfile);
//            LegacyFileManager.getInstance().loadFiles(LegacyFileManager.WhichFile.IP,false);
//        } catch (IOException e) {
//            getProxy().getLogger().warning("Unable to load ips. ");
//        }

        //ADVANCEDBAN HOOK
        if (hookManager.hasAdvancedBan()) {
            getLogger().info(ChatColor.GREEN + "FOUND ADVANCEDBAN! HOOKING IN API");
            getProxy().getPluginManager().registerListener(this, new AltsBan());
        }

        //////////////////////////////////////////////////getProxy().wegetPluginManager().registerListener(this,new AskPlaceHolder());

        //getProxy().getPluginManager().registerListener(this,new UUIDSpoofChecker()  );

        Universal.getMessageHandler().registerMessageHandler(new NickNetworkManager());
        getLogger().info("Registered fern nicks bungee channels.");

        getProxy().getPluginManager().registerListener(this, new PunishMOTD());

        //MAIN FERN COMMAND MANAGER
//        getProxy().getPluginManager().registerCommand(this, new FernMain());

        Universal.getCommandHandler().registerCommand(new FernMain());

        if(FileManager.getConfigValues().isAllowIPShow()) {
//            getProxy().getPluginManager().registerCommand(this,new MainIP());
            Universal.getCommandHandler().registerCommand(new MainIP());
        }
        if(FileManager.getConfigValues().isShowAltsCommand()) {
//            getProxy().getPluginManager().registerCommand(this, new ShowAlts());
            Universal.getCommandHandler().registerCommand(new ShowAlts());
        }

        if(FileManager.getConfigValues().isAllowIPDelete()) {
            MainIP.loadTasks();
        }

        PlatformAllRegistration.registerCommands();

        if(FileManager.getConfigValues().isAllowNameHistory()) {
//            getProxy().getPluginManager().registerCommand(this, new NameHistory());
            Universal.getCommandHandler().registerCommand(new NameHistory());
        }
        if(FileManager.getConfigValues().isGlobalNicks() && configValues.isDatabaseConnect()) {
//            getProxy().getPluginManager().registerCommand(this, new FernNick());
            Universal.getCommandHandler().registerCommand(new FernNick());
        }

//        getProxy().getPluginManager().registerCommand(this,new GetPlaceholderCommand("bpapi","fernc.bpapi"));
        Universal.getCommandHandler().registerCommand(new GetPlaceholderCommand());


        getLogger().info("Registered fern nicks");

    }



    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "SAVING FILES");
        FileManager.configSave(FileManager.getIpConfig());
        FileManager.configSave(FileManager.getSeenConfig());
        FileManager.configSave(FileManager.getConfigManager());
        FileManager.configSave(FileManager.getDeleteIPConfig());
        getLogger().info(ChatColor.GREEN + "FILED SUCCESSFULLY SAVED");

        super.onDisable();
        instance = this;
        getLogger().info(ChatColor.GREEN + "DISABLED FERNCOMMANDS FOR BUNGEECORD");

        HookManager.onDisable();
        instance = null;
    }




    public BaseComponent message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create()[0];
    }


    public void printInLog(Object classe, Object log) {
        getLogger().info("{" + classe.getClass().getName() + "} " + log);
    }




    public static FernCommands getInstance() {
        return instance;
    }
}
