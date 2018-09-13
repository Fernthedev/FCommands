package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.Universal.UUIDFetcher;
import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import io.github.fernthedev.fcommands.bungeeclass.FileManager;
import io.github.fernthedev.fcommands.bungeeclass.hooks;
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
        if (hooks.getInstance().hasAdvancedBan()) {
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
                List<String> players = ipconfig.getStringList(ip);

                //List<String> ips = new ArrayList<>();
                //ips.add(ip);

                for (String ipe : ipconfig.getKeys()) {
                    if (!ipe.equals(ip)) {
                        List<String> playips = ipconfig.getStringList(ipe);
                        if (!playips.isEmpty() && playips.contains(uuidPlayer)) {
                            for (String pluuid : playips) {
                                pluuid = pluuid.replace("-","");

                                if (!players.contains(pluuid)) {

                                    players.add(pluuid);
                                }
                            }
                        }
                    }
                }


                for (String uuid : players) {
                    String playername = UUIDFetcher.getName(uuid);
                    FernCommands.getInstance().getLogger().info("Player is " + uuid + " and is going to get punishment from player " + e.getPlayer().getName());
                    //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);

                    if (PunishmentManager.get().isBanned(uuid) || PunishmentManager.get().isMuted(uuid)) {
                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.MUTE, true).isEmpty()) {
                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.MUTE, true)) {

                                Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                    punishment1.create(true);

                            }
                        }

                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_MUTE, true).isEmpty()) {
                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_MUTE, true)) {

                                Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                    punishment1.create(true);

                            }
                        }

                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.BAN, true).isEmpty()) {

                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.BAN, true)) {
                                Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                punishment1.create(true);

                            }
                        }

                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_BAN, true).isEmpty()) {
                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_BAN, true)) {
                                Punishment punishment1 = new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                if(!PunishmentManager.get().getPunishments(uuid,punishment.getType(),true).contains(punishment1))
                                punishment1.create(true);

                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onPunish(PunishmentEvent e) {
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


        FernCommands.getInstance().printInLog(this, "Player UUID " + uuidPlayer + " is " + name + " and is getting others punished.");

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

            List<String> players = new ArrayList<>();

            List<String> ips = new ArrayList<>();

            //List<String> ips = new ArrayList<>();
            //ips.add(ip);

            for (String ipe : ipconfig.getKeys()) {
                // FernCommands.getInstance().printInLog(this,ipe + " is the ip being checked now.");
                List<String> playips = ipconfig.getStringList(ipe);
                if (!playips.isEmpty() && playips.contains(uuidPlayer)) {
                    ips.add(ipe);
                }
            }

            for(String ipe : ips) {
                List<String> playips = ipconfig.getStringList(ipe);
                for(String pluuid : playips) {
                    print("The ip being checked is " + ipe + " and the uuid being checked is " + pluuid);
                    if (!players.contains(pluuid)) {
                        players.add(pluuid);
                    }
                }
            }

            print("Players list is " + players.toString());

            for (String uuid : players) {
                print("The uuid is " + uuid);
                String playername;
                if(ProxyServer.getInstance().getPlayer(uuid) != null) {
                    playername = ProxyServer.getInstance().getPlayer(uuid).getName();
                }else {
                    playername = UUIDFetcher.getName(uuid);
                }

                print("A uuid is " + uuid + " and name is " + playername + " punishing now.");
                if(playername == null) return;

                Punishment punishment = new Punishment(playername, uuid, e.getPunishment().getReason() + " &6From an alt which is " + ProxyServer.getInstance().getPlayer(e.getPunishment().getUuid()), e.getPunishment().getOperator(), e.getPunishment().getType(), e.getPunishment().getStart(), e.getPunishment().getEnd(), e.getPunishment().getCalculation(), -1);
                if (!PunishmentManager.get().getPunishments(uuid, punishment.getType(), true).contains(punishment))
                    punishment.create(true);
            }
        } else {
            FernCommands.getInstance().printInLog(this, "Failed to load ips. UUID Player is " + uuidPlayer);
        }

    }

    /*
    public String getNameByUUI(String uuid) {
        URL url = null;
        InputStreamReader in = null;
        try {
            url = new URL("https://api.mojang.com/user/profiles/UUID/names".replace("UUID", uuid.replace("-", "")));
            try {
                in = new InputStreamReader(url.openStream());
                return FernCommands.getGson().fromJson(in, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(in != null)
                    in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }*/


    public void print(Object log) {
        if(log == null) log = "null because the object was null";
        FernCommands.getInstance().printInLog(this,log);
    }
}
