package com.github.fernthedev.fcommands.bungeeclass.commands.ip;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DeleteIP extends Command {

    private static DeleteIP ourInstance = new DeleteIP();

    public static DeleteIP getInstance() {
        return ourInstance;
    }

    public DeleteIP() {
        super("myip","fernc.myip.delete","me");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;


        Configuration ipconfig = new FileManager().getIpconfig();

        String ipe = p.getAddress().getHostString();
        final String ip = ipe.replaceAll("\\.", " ");




        boolean ipfileloaded;

        try {

            new FileManager().loadFiles(FileManager.WhichFile.IP,true);
            new FileManager().loadFiles(FileManager.WhichFile.DELETEIP,true);

            ipfileloaded = true;
        } catch (IOException e) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }



        Configuration deleteipconfig = new FileManager().getDeleteipconfig();

        if(deleteipconfig.getSection(ip).getBoolean("isToDelete")) {
            p.sendMessage(messager("&aYou have already asked to delete your ip"));
        }else
        if(ipfileloaded) {
            deleteipconfig.set(ip,null);
            deleteipconfig.getSection(ip).set("isToDelete",true);
            deleteipconfig.getSection(ip).set("Deleted",false);
            deleteipconfig.getSection(ip).set("Requested",p.getUniqueId().toString());

            saveDeleteFile(deleteipconfig);


            Timer timer = new Timer();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR, 1); // adds one hour
            //cal.getTime(); // returns new date object, one hour in the future

            Date date = cal.getTime();
            String setTimee =dateFormat.format(date);

            FernCommands.getInstance().getLogger().info("The time to hide delete is " + setTimee);

            setTimee = setTimee.replaceAll("/","-");

            deleteipconfig.getSection(ip).set("HideDelete",setTimee);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    deleteipconfig.getSection(ip).set("Deleted",true);
                    saveDeleteFile(deleteipconfig);
                    FernCommands.getInstance().getLogger().info("Made ip invisible to player " + ip + " requested by " + p.getDisplayName());
                }
            }, TimeUnit.HOURS.toMillis(1));


            Calendar cal2 = Calendar.getInstance(); // creates calendar
            cal2.setTime(new Date()); // sets calendar time/date
            cal2.add(Calendar.WEEK_OF_MONTH, 1); // adds 7 days
            date = cal2.getTime(); // returns new date object, one hour in the future

            setTimee =dateFormat.format(date);


            FernCommands.getInstance().getLogger().info("The time to delete is " + setTimee);
            setTimee = setTimee.replaceAll("/","-");
            deleteipconfig.getSection(ip).set("Delete",setTimee);


            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig,FernCommands.getIpdeletefile());
                //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
            } catch (IOException ee) {
                ee.printStackTrace();
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    deleteipconfig.getSection(ip).set("isToDelete",false);
                    deleteipconfig.set(ip,null);
                    ipconfig.set(ip,null);
                    saveDeleteFile(deleteipconfig);

                    try {
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig,FernCommands.getIpfile());
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig,FernCommands.getIpdeletefile());
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







    public static void loadTasks() {
        Configuration ipconfig = new FileManager().getIpconfig();




        boolean ipfileloaded;

        try {

            new FileManager().loadFiles(FileManager.WhichFile.IP,true);
            new FileManager().loadFiles(FileManager.WhichFile.DELETEIP,true);

            ipfileloaded = true;
        } catch (IOException e) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }



        Configuration deleteipconfig = new FileManager().getDeleteipconfig();

        if(ipfileloaded) {
            for(String key : deleteipconfig.getKeys()) {
                String dateString = deleteipconfig.getSection(key).getString("HideDelete").replaceAll("-","/");
                String deleteString = deleteipconfig.getSection(key).getString("Delete").replaceAll("-","/");
                Timer timer = new Timer();
                Date hideDate = null;
                Date deleteDate = null;

                try {
                    FernCommands.getInstance().getLogger().info("The date string is " + dateString);
                    hideDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateString);
                    deleteDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(deleteString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                /////////////////////////////////////////// HIDE DATE
                if(hideDate != null) {
                    if(hideDate.before(new Date())) {

                        deleteipconfig.getSection(key).set("Deleted", true);

                        try {
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig, FernCommands.getIpdeletefile());
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig,FernCommands.getIpfile());
                            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                        } catch (IOException ee) {
                            ee.printStackTrace();
                        }
                        FernCommands.getInstance().getLogger().info("Made ip invisible to player " + key + " requested by " + deleteipconfig.getString("Requested"));

                    }else {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                deleteipconfig.getSection(key).set("Deleted", true);

                                try {
                                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig, FernCommands.getIpdeletefile());
                                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig,FernCommands.getIpfile());
                                    //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                                } catch (IOException ee) {
                                    ee.printStackTrace();
                                }

                                FernCommands.getInstance().getLogger().info("Made ip invisible to player " + key + " requested by " + ProxyServer.getInstance().getPlayer(deleteipconfig.getString("Requested")).getDisplayName());
                            }
                        }, hideDate);
                    }
                }


                ////////////////////////////////////////////////////////////
                if(deleteDate != null) {
                    if (deleteDate.before(new Date())) {

                        deleteipconfig.getSection(key).set("isToDelete", false);
                        deleteipconfig.set(key, null);
                        ipconfig.set(key, null);

                        try {
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig, FernCommands.getIpdeletefile());
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig, FernCommands.getIpfile());
                            //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                        } catch (IOException ee) {
                            ee.printStackTrace();
                        }


                        FernCommands.getInstance().getLogger().info("Deleted ip on all files " + key + " requested by " + deleteipconfig.getString("Requested"));

                    } else {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                deleteipconfig.getSection(key).set("isToDelete", false);
                                deleteipconfig.set(key, null);
                                ipconfig.set(key, null);

                                try {
                                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(deleteipconfig, FernCommands.getIpdeletefile());
                                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(ipconfig, FernCommands.getIpfile());
                                    //ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
                                } catch (IOException ee) {
                                    ee.printStackTrace();
                                }


                                FernCommands.getInstance().getLogger().info("Deleted ip on all files " + key + " requested by " + deleteipconfig.getString("Requested"));
                            }
                        }, deleteDate);
                    }
                }
            }
        }
    }

}
