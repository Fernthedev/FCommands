package io.github.fernthedev.fcommands.bungeeclass.placeholderapi;


import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import io.github.fernthedev.fcommands.bungeeclass.MessageRunnable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AskPlaceHolder implements Listener {

    private ProxiedPlayer player;
    private String placeHolderValue;

    boolean checked;
    boolean placeHolderReplaced;

    private String oldPlaceValue;

    private int counted;
    private Timer taske;

    private MessageRunnable runnable;

    private static List<AskPlaceHolder> instances = new ArrayList<>();

    private boolean runnableset = false;
    private String uuid;

    private ByteArrayOutputStream outputStream;


    public AskPlaceHolder() {
        FernCommands.getInstance().getLogger().info("Registered PlaceHolderAPI Listener");
    }

    public String getPlaceHolderResult() {
        return placeHolderValue;
    }





    public AskPlaceHolder(ProxiedPlayer player, String placeHolderValue) {
     this.player = player;


     FernCommands.getInstance().getLogger().info("Requested to ask a placeholder from " + player.getServer().getInfo().getName());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF("Forward"); //TYPE
            out.writeUTF(player.getServer().getInfo().getName()); //SERVER
            out.writeUTF("GetPlaceHolderAPI"); //SUBCHANNEL

            uuid = UUID.randomUUID().toString();
            if(!instances.isEmpty()) {
                for(AskPlaceHolder askPlaceHolder : instances) {
                    while(askPlaceHolder.uuid.equals(uuid)) {
                        uuid = UUID.randomUUID().toString();
                    }
                }
            }


            getLogger().info("Current uuid is " + uuid);

            out.writeUTF(placeHolderValue); //MESSAGE 1 (placeholder requested)
            oldPlaceValue = placeHolderValue;
            out.writeUTF(uuid); //MESSAGE 2 (UUID)

        } catch (IOException e) {
            e.printStackTrace();
        }



        Server server = player.getServer();
        FernCommands.getInstance().getLogger().info("Placeholder requested to " + server.getInfo().getName() + " for placeholder " + placeHolderValue);

        outputStream = stream;

        FernCommands.getInstance().getLogger().info("Request sent");
        checked = false;
    }

    public void setRunnable(MessageRunnable mrunnable) {
        runnableset = true;
        this.runnable = mrunnable;
        instances.add(this);

        player.getServer().sendData("BungeeCord",outputStream.toByteArray());
        getLogger().info("Runnable has now been initialized");
    }

    public boolean isPlaceHolderReplaced() {
        return placeHolderReplaced;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent ev) {
        if (ev.getTag().equalsIgnoreCase("Bungeecord")) {
            getLogger().info("It is a bungeecord message");
            //getLogger().info("Sender is " + ev.getSender());
            //FernCommands.getInstance().getLogger().info("Requested message from " + ev.getSender().getAddress());
            if (ev.getSender() instanceof Server) {
                FernCommands.getInstance().getLogger().info("It is from current server.");
                ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
                DataInputStream in = new DataInputStream(stream);
                try {
                    String channel = in.readUTF(); // channel we delivered
                    String server = in.readUTF();
                    String subchannel = in.readUTF();

                    if (channel.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase("PlaceHolderValue")) {

                        String placeholder = in.readUTF();
                        String uuide = in.readUTF();

                        getLogger().info("Channel is " + channel + " and server is" + server + " and subchannel is " + subchannel + " and placeholder is " + placeholder);

                        getLogger().info("The message is for a placeholder. Running code!");

                        AskPlaceHolder instance = null;
                        if(!instances.isEmpty()) {
                            for (AskPlaceHolder askPlaceHolder : instances) {
                                //getLogger().info("A uuid is" + askPlaceHolder.uuid);
                                if (askPlaceHolder.uuid.equals(uuide)) {
                                    instance = askPlaceHolder;
                                }
                            }
                        }else{
                         getLogger().info("There were no instances");
                        }
                        if(instance != null) {
                            instance.placeHolderValue = placeholder;
                            instance.placeHolderReplaced = !instance.placeHolderValue.equals(instance.oldPlaceValue);
                            instance.checked = true;
                            if (instance.runnableset) {
                                getLogger().info("Runnable is set");
                            } else {
                                getLogger().info("Runnable is not set");
                            }
                            instance.runTask();
                            removeInstance(instance);
                        }else{
                            getLogger().info(ChatColor.RED + "The incoming message was not expected. From an attacker?");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void removeInstance(AskPlaceHolder askPlaceHolder) {
        getLogger().info("Removed an instance from list");
        instances.remove(askPlaceHolder);
    }

    @SuppressWarnings("unused")
    private void removeInstance() {
        getLogger().info("Removed an instance from themselves to the list");
        instances.remove(this);
    }

    private void cancelTask() {
        getLogger().info("Task cancelled");
        taske.cancel();
        taske.purge();
    }

    public void runTask() {
        counted = 0;
        taske = new Timer();

        taske.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                    getLogger().info("Ran " + counted);
                    if (counted <= 10) {
                        if (runnableset) {
                            runnable.run();
                            cancelTask();
                        } else {
                            counted++;
                        }
                    } else {
                        cancelTask();
                        if(player != null) {
                            player.sendMessage(FernCommands.getInstance().message("&cThere was an error trying to run this command."));
                        }
                        getLogger().info("There was an error trying to run this command.");
                    }
                }

        }, TimeUnit.SECONDS.toMillis(2),TimeUnit.SECONDS.toMillis(2));


    }


    private Logger getLogger() {
        return FernCommands.getInstance().getLogger();
    }

    /*
    @SuppressWarnings("Unused")
    public void sendToBukkit(String channel, String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("Return", stream.toByteArray());
    }*/
}