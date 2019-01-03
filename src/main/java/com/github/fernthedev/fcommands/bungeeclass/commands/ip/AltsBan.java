package com.github.fernthedev.fcommands.bungeeclass.commands.ip;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import com.github.fernthedev.fcommands.bungeeclass.HookManager;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AltsBan implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent e) {

        if (HookManager.getInstance().hasAdvancedBan()) {
            ProxiedPlayer player = e.getPlayer();
            Configuration ipconfig = new FileManager().getIpconfig();

            String uuidPlayer = player.getUniqueId().toString();

            String ip = player.getAddress().getHostString();
            ip = ip.replaceAll("\\.", " ");


            //String uuid = p.getUniqueId().toString();

            boolean ipfileloaded;

            try {
                new FileManager().loadFiles("ip", true);
                ipfileloaded = true;
            } catch (IOException ee) {
                FernCommands.getInstance().getLogger().warning("Unable to load ips");
                ipfileloaded = false;
            }

            if (ipfileloaded) {

                for (String ipe : ipconfig.getKeys()) {
                    if (!ipe.equals(ip)) {
                        List<String> playips = ipconfig.getStringList(ipe);
                        if (!playips.isEmpty() && playips.contains(uuidPlayer)) {
                            for (String pluuid : playips) {


                                String uuid = pluuid.replace("-","");

                                String playername = UUIDFetcher.getName(uuid);
                                FernCommands.getInstance().getLogger().info("Player is " + uuid + " and is going to get punishment from player " + e.getPlayer().getName());
                                //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);

                                if (PunishmentManager.get().isBanned(uuid) || PunishmentManager.get().isMuted(uuid)) {
                                    if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.MUTE, true).isEmpty()) {
                                        for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.MUTE, true)) {

                                            Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                            if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                                punishment1.create(true);

                                        }
                                    }

                                    if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_MUTE, true).isEmpty()) {
                                        for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_MUTE, true)) {

                                            Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                            if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                                punishment1.create(true);

                                        }
                                    }

                                    if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.BAN, true).isEmpty()) {

                                        for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.BAN, true)) {
                                            Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                            if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                                punishment1.create(true);

                                        }
                                    }

                                    if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_BAN, true).isEmpty()) {
                                        for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_BAN, true)) {
                                            Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                            if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                                punishment1.create(true);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void onPunish(PunishmentEvent e) {
        if(e.getPunishment().getType() == PunishmentType.KICK || e.getPunishment().getType() == PunishmentType.WARNING) return;
/*
        if(player == null) {
            FernCommands.getInstance().getLogger().info("There was an issue finding a punished player with the uuid of " + e.getPunishment().getUuid() + " and name is " + e.getPunishment().getName());

            return;
        }else punishedUUID = player.getUniqueId().toString();

        Configuration ipconfig = new FileManager().getIpconfig();

        String uuidPlayer = player.getUniqueId().toString();*/

        //String ip = player.getAddress().getHostString();
        //ip = ip.replaceAll("\\.", " ");


        //String uuid = p.getUniqueId().toString();


        String uuidPlayer = e.getPunishment().getUuid();

        String name = UUIDFetcher.getName(uuidPlayer);

        String uuidPlayerUUID = UUIDFetcher.getUUID(e.getPunishment().getName());

        if (uuidPlayerUUID != null) uuidPlayer = uuidPlayerUUID;
        else uuidPlayer = null;


        print("Player UUID " + uuidPlayer + " is " + name + " and is getting others punished.");

        boolean ipfileloaded;

        try {
            new FileManager().loadFiles("ip", true);
            ipfileloaded = true;
        } catch (IOException ee) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }

        if (ipfileloaded && uuidPlayer != null) {
            Configuration ipconfig = new FileManager().getIpconfig();

            List<String> players;
            List<String> ips = new ArrayList<>();


            players = new ArrayList<>();
                players.add(uuidPlayer);




            for(String ipe : ipconfig.getKeys()) {
                    List<String> playips = ipconfig.getStringList(ipe);
                   //print("The players for ip " + ipe + " has uuids " + playips.toString());

                    if (!playips.isEmpty()) {
                        //ips.add(ipe);
                        boolean keepgoing = true;
                        for (String pluuid : playips) {
                            if (keepgoing) {
                                pluuid = pluuid.replaceAll("-", "");
                                //print("The ip " + ipe + " has player uuid " + uuidPlayer + " with name " + name);
                                if (pluuid.equals(uuidPlayer)) {
                                    ips.add(ipe);
                                    keepgoing = false;
                                }
                            }
                        }
                    }
            }

            List<String> bannedPlayers = new ArrayList<>();
            bannedPlayers.add(uuidPlayer);

            for(String ipe : ips) {
                List<String> playips = ipconfig.getStringList(ipe);
                for(String pluuid : playips) {
                    pluuid = pluuid.replaceAll("-","");
                    if(!players.contains(pluuid)) {
                        players.add(pluuid);
                        print("Ip " + ipe + " has player " + pluuid + " being added to the list.");


                        if (!bannedPlayers.contains(pluuid)) {
                            print("The uuid is " + pluuid);
                            String playername;
                            if (ProxyServer.getInstance().getPlayer(pluuid) != null) {
                                playername = ProxyServer.getInstance().getPlayer(pluuid).getName();
                            } else {
                                playername = UUIDFetcher.getName(pluuid);
                            }

                            print("A uuid is " + pluuid + " and name is " + playername + " punishing now.");
                            if (playername != null) {

                                Punishment punishment = new Punishment(playername, pluuid, e.getPunishment().getReason() + " &6From an alt which is " + UUIDFetcher.getName(e.getPunishment().getUuid()), e.getPunishment().getOperator(), e.getPunishment().getType(), e.getPunishment().getStart(), e.getPunishment().getEnd(), e.getPunishment().getCalculation(), -1);

                                if (!PunishmentManager.get().getPunishments(pluuid, punishment.getType(), true).contains(punishment)) {
                                    punishment.create(true);
                                    bannedPlayers.add(pluuid);
                                } else {
                                    bannedPlayers.add(pluuid);
                                }
                            }
                        }



                    }
                }
            }



            print("Players list is " + players.toString());

            print("Banned players are " + bannedPlayers.toString());
        } else {
            FernCommands.getInstance().printInLog(this, "Failed to load ips. UUID Player is " + uuidPlayer);
        }

    }


    public void print(Object log) {
        if(log == null) log = "null because the object was null";
        FernCommands.getInstance().printInLog(this,log);
    }
}
