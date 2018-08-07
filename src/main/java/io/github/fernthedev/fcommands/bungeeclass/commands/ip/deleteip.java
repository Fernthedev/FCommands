package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import io.github.fernthedev.fcommands.bungeeclass.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class deleteip extends Command {

    private static deleteip ourInstance = new deleteip();

    public static deleteip getInstance() {
        return ourInstance;
    }

    public deleteip() {
        super("myip","fernc.myip.delete","me");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;


        Configuration ipconfig = new FileManager().getIpconfig();

        String ipe = p.getAddress().getHostString();
        final String ip = ipe.replaceAll("\\.", " ");


        //String uuid = p.getUniqueId().toString();

        boolean ipfileloaded;

        try {
            //ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipfile);
            new FileManager().loadFiles("ip",true);
            new FileManager().loadFiles("ipdelete",true);
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
            ipfileloaded = true;
        } catch (IOException e) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }

        List<String> players = ipconfig.getStringList(ip);

        Configuration deleteipconfig = new FileManager().getDeleteipconfig();

        if(deleteipconfig.getSection(ip).getBoolean("isToDelete")) {
            p.sendMessage(messager("&aYou have already asked to delete your ip"));
        }else
        if(ipfileloaded) {
            deleteipconfig.set(ip,null);
            deleteipconfig.getSection(ip).set("isToDelete",true);
            deleteipconfig.getSection(ip).set("Deleted",false);

            saveDeleteFile(deleteipconfig);


            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    deleteipconfig.getSection(ip).set("Deleted",true);
                    saveDeleteFile(deleteipconfig);
                    FernCommands.getInstance().getLogger().info("Made ip invisible to player " + ip + " requested by " + p.getDisplayName());
                }
            }, TimeUnit.HOURS.toMillis(1));

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    deleteipconfig.getSection(ip).set("isToDelete",false);
                    deleteipconfig.set(ip,null);
                    ipconfig.set(ip,null);
                    saveDeleteFile(deleteipconfig);

                    try {
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig,FernCommands.getIpfile());
                        //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }

                    FernCommands.getInstance().getLogger().info("Deleted ip on all files " + ip + " requested by " + p.getDisplayName());
                }
            },TimeUnit.DAYS.toMillis(7));

            p.sendMessage(messager("&aSuccessfully requested delete. It will be deleted in at least an hour"));
        }
    }

    private void saveDeleteFile(Configuration deleteipconfig) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig,FernCommands.getIpdeletefile());
            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    private BaseComponent[] messager(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
