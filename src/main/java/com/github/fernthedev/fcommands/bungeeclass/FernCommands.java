package com.github.fernthedev.fcommands.bungeeclass;


import com.github.fernthedev.fcommands.Universal.Channels;
import com.github.fernthedev.fcommands.bungeeclass.commands.*;
import com.github.fernthedev.fcommands.bungeeclass.commands.ip.AltsBan;
import com.github.fernthedev.fcommands.bungeeclass.commands.ip.DeleteIP;
import com.github.fernthedev.fcommands.bungeeclass.commands.ip.MainIP;
import com.github.fernthedev.fcommands.bungeeclass.commands.ip.ShowAlts;
import com.github.fernthedev.fcommands.bungeeclass.placeholderapi.AskPlaceHolder;
import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@SuppressWarnings("unused")
public class FernCommands extends FernBungeeAPI {

    //public static File pluginFolder;
    private static File ipfile;
    private static File seenfile;
    private static File ipdeletefile;
    private static Gson gson;
   // private static Configuration IpDataConfig;
    //private static Configuration configuration;
    //private static ConfigurationProvider configp;

    //private static File configfile;
    private static FernCommands instance;

    private Universal universal;

    private static Connection connection;

    private static List<Runnable> runnableList = new ArrayList<>();

    public static List<Runnable> getRunnables() {
        return runnableList;
    }


    @Override
   public void onEnable() {
        super.onEnable();
        instance = this;
        gson = new Gson();
        getLogger().info(ChatColor.BLUE + "ENABLED FERNCOMMANDS FOR BUNGEECORD");
        ipfile = new File(getDataFolder(), "ipdata.yml");
        seenfile = new File(getDataFolder(), "Seen.yml");
        ipdeletefile = new File(FernCommands.getInstance().getDataFolder(),"ipdelete.yml");
        //configfile = new File(getDataFolder(), "config.yml");
        //configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
        new HookManager();
        new FileManager();
        if (!getDataFolder().exists()) {
            boolean mkdir = getDataFolder().mkdir();
            System.out.println(mkdir);
        }

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {

            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            FileManager.getInstance().loadFiles("config",false);
            getLogger().info("Attempting to connect to MySQL");
            DatabaseHandler.setup();
            getLogger().info("Mysql connection attempted.");
            connection = DatabaseHandler.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }




        //CREATING SEEN FILE
        FileManager.getInstance().createseenFile();
        try {
            FileManager.getInstance().loadFiles("Seen",false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        //File ipfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        //File seenfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");


        getProxy().getPluginManager().registerListener(this, new ServerMaintenanceMOTD());
        //SEEN COMMAND
        if(FileManager.getConfigValues().isAllowSeenCommand()) {
            getProxy().getPluginManager().registerCommand(this, new Seen());
            getProxy().getPluginManager().registerListener(this, new Seen());
        }

        try {
            FileManager.getInstance().createipFile();
            //Configuration IpDataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            //FileManager.getInstance().loadFile(ipfile);
            FileManager.getInstance().loadFiles("ip",false);
        } catch (IOException e) {
            getProxy().getLogger().warning("Unable to load ips. ");
        }

        //ADVANCEDBAN HOOK
        if (HookManager.getInstance().hasAdvancedBan()) {
            getProxy().getLogger().info(ChatColor.GREEN + "FOUND ADVANCEDBAN! HOOKING IN API");
            getProxy().getPluginManager().registerListener(this,new AltsBan());
        }

        getProxy().getPluginManager().registerListener(this,new AskPlaceHolder());
        //getProxy().getPluginManager().registerListener(this,new UUIDSpoofChecker()  );
        getProxy().registerChannel("GetPlaceHolderAPI");
        getProxy().registerChannel("PlaceHolderValue");

        getProxy().registerChannel(Channels.PlaceHolderBungeeChannel);

        getLogger().info("Registered PlaceHolderAPI channels");
        getProxy().registerChannel("ReloadNickSQL");
        getProxy().getPluginManager().registerListener(this,new FernNick());
        getLogger().info("Registered fern nicks bungee channels.");

        getProxy().getPluginManager().registerListener(this, new PunishMOTD());

        try {
            UUIDFetcher.addRequestTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //MAIN FERN COMMAND MANAGER
        getProxy().getPluginManager().registerCommand(this, new FernMain());

        if(FileManager.getConfigValues().isAllowIPShow()) {
            getProxy().getPluginManager().registerCommand(this,new MainIP());
        }
        if(FileManager.getConfigValues().isShowAltsCommand()) {
            getProxy().getPluginManager().registerCommand(this, new ShowAlts());
        }

        if(FileManager.getConfigValues().isAllowIPDelete()) {
            DeleteIP.loadTasks();
        }

        if(FileManager.getConfigValues().isShowPing()) {
            getProxy().getPluginManager().registerCommand(this, new FernPing());
        }

        if(FileManager.getConfigValues().isAllowNameHistory()) {
            getProxy().getPluginManager().registerCommand(this, new NameHistory());
        }
        if(FileManager.getConfigValues().isGlobalNicks()) {
            getProxy().getPluginManager().registerCommand(this, new FernNick());
        }

        getProxy().getPluginManager().registerCommand(this,new GetPlaceholderCommand("bpapi","fernc.bpapi"));


        getLogger().info("Registered fern nicks");

        run();

    }



    @Override
    public void onDisable(){
        instance = this;
        getLogger().info(ChatColor.GREEN + "DISABLED FERNCOMMANDS FOR BUNGEECORD");

        DatabaseHandler.getScheduler().cancel(this);
        // invoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (connection!=null && !connection.isClosed()){ //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        //weeeeeeeeeeeeeeeee

        ipfile = null;
        seenfile = null;
        FernMain.onDisable();
        UUIDFetcher.stopRequestTimer();
        UUIDFetcher.stopHourTask();
        Seen.onDisable();
        HookManager.onDisable();
        FileManager.onDisable();
        instance = null;
    }



    public static File getIpfile() {
        return ipfile;
    }




    public BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

    private void run() {
        getProxy().getScheduler().schedule(this, () -> {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            } catch (IOException e) {
                getLogger().warning("unable to reload config");
            }
        }, 3L, TimeUnit.SECONDS);
    }


    public void printInLog(Object classe, Object log) {
        getLogger().info("{" + classe.getClass().getName() + "} " + log);
    }


    public static Gson getGson() {
        return gson;
    }
    public static File getIpdeletefile() {
        return ipdeletefile;
    }

    public File getSeenfile() {
        return seenfile;
    }

    @Nonnull
    public static FernCommands getInstance() {
        return instance;
    }
}
