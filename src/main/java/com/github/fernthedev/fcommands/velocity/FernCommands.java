package com.github.fernthedev.fcommands.velocity;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.commands.FernMain;
import com.github.fernthedev.fcommands.proxy.commands.FernNick;
import com.github.fernthedev.fcommands.proxy.commands.GetPlaceholderCommand;
import com.github.fernthedev.fcommands.proxy.commands.Seen;
import com.github.fernthedev.fcommands.proxy.commands.ip.MainIP;
import com.github.fernthedev.fcommands.proxy.commands.ip.ShowAlts;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.universal.DBManager;
import com.github.fernthedev.fcommands.universal.NickNetworkManager;
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration;
import com.github.fernthedev.fcommands.universal.UniversalMysql;
import com.github.fernthedev.fcommands.universal.commands.NameHistory;
import com.github.fernthedev.fcommands.velocity.commands.VelocityPluginList;
import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;

@Plugin(id = "fern_commands", name = "FernCommands", version = "${version}",
        description = "A suite of stuff for Fern server", authors = {"Fernthedev"},
        dependencies = @Dependency(id = "preference_manager")
)
public class FernCommands extends FernVelocityAPI {

    private static FernCommands instance;

    @Getter
    private static DBManager databaseManager;

    @Inject
    public FernCommands(ProxyServer server, Logger logger) {
        super(server, logger);
    }


    @Getter
    private static FileManager fileManager;

    @Subscribe
    @Override
    public void onProxyInitialization(ProxyInitializeEvent event) {
        super.onProxyInitialization(event);

        instance = this;
        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR VELOCITY");

        //configfile = new File(getDataFolder(), "config.yml");
        //configp = ConfigurationProvider.getProvider(YamlConfiguration.class);

        File dataFolder = dataDirectory.toFile();

        if (!dataFolder.exists()) {
            boolean mkdir = dataFolder.mkdir();
            getLogger().info(mkdir + " folder make");
        }
        fileManager = new FileManager();


        server.getCommandManager().register("vplugins",new VelocityPluginList(server));
        server.getEventManager().register(this, new Events());

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
        server.getEventManager().register(this, maintenanceMOTD);
        //SEEN COMMAND
        if(FileManager.getConfigValues().isAllowSeenCommand()) {
            Universal.getCommandHandler().registerCommand(new Seen());
        }



        //////////////////////////////////////////////////getProxy().wegetPluginManager().registerListener(this,new AskPlaceHolder());

        //getProxy().getPluginManager().registerListener(this,new UUIDSpoofChecker()  );

        Universal.getMessageHandler().registerMessageHandler(new NickNetworkManager());
        getLogger().info("Registered fern nicks velocity channels.");

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

        PlatformAllRegistration.commonInit();

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
    public void onProxyStop(ProxyShutdownEvent event) {
        super.onProxyStop(event);
        getLogger().info(ChatColor.GREEN + "SAVING FILES");
        FileManager.configSave(FileManager.getIpConfig());
        FileManager.configSave(FileManager.getSeenConfig());
        FileManager.configSave(FileManager.getConfigManager());
        FileManager.configSave(FileManager.getDeleteIPConfig());
        getLogger().info(ChatColor.GREEN + "FILED SUCCESSFULLY SAVED");

        instance = this;
        getLogger().info(ChatColor.GREEN + "DISABLED FERNCOMMANDS FOR BUNGEECORD");

        instance = null;
    }


    public static FernCommands getInstance() {
        return instance;
    }
}
