package com.github.fernthedev.fcommands.bungee.commands.ip;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.bungee.FernCommands;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import lombok.NonNull;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AltsBan implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent e) {
        if (FernCommands.getHookManager().hasAdvancedBan()) {
            Config<IPSaveValues> ipConfig = FileManager.getIpConfig();
            FileManager.configLoad(ipConfig);

            @NonNull IPSaveValues values = ipConfig.getConfigData();

            Universal.getScheduler().runAsync(() -> {
                ProxiedPlayer player = e.getPlayer();

                String uuidPlayer = player.getUniqueId().toString();

                String ip = player.getAddress().getHostString().replaceAll("\\.", " ");


                //String uuid = p.getUniqueId().toString();



                List<Runnable> runnablesToDo = new ArrayList<>();



//                System.out.println(values + " is not null");

                for (String ipe : values.getPlayerMap().keySet()) {
                    if (!ipe.equals(ip)) {
                        List<UUID> playerIPs = values.getPlayers(ipe);
                        if (playerIPs != null && !playerIPs.isEmpty()) {

                            boolean contained = false;

                            for (UUID uuid : playerIPs) {
                                if (uuid.toString().replaceAll("-", "").equals(uuidPlayer.replaceAll("-", ""))) {
                                    contained = true;
                                    break;
                                }
                            }

                            if(!contained) return;


                            int runnablesPerThread = playerIPs.size() / 4;

                            int runnablesDone = 0;

                            for (UUID uuid : playerIPs) {

                                String formattedUUID = uuid.toString().replaceAll("-","");

                                runnablesDone++;

                                if (runnablesDone > runnablesPerThread) {
                                    runnablesDone = 0;
                                    List<Runnable> runnableList = new ArrayList<>(runnablesToDo);
                                    ProxyServer.getInstance().getScheduler().runAsync(FernCommands.getInstance(), () -> {
                                        for (Runnable runnable : runnableList)
                                            runnable.run();
                                    });
                                    runnablesToDo.clear();
                                }

                                runnablesToDo.add(() -> {
                                    String playerName = UUIDFetcher.getName(formattedUUID);
                                    if (PunishmentManager.get().isBanned(formattedUUID) || PunishmentManager.get().isMuted(formattedUUID)) {

                                        if (!PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.MUTE, true).isEmpty()) {
                                            for (Punishment punishment : PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.MUTE, true)) {

                                                Punishment punishment1 = new Punishment(playerName, formattedUUID, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                                if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment1))
                                                    punishment1.create(true);

                                            }
                                        }

                                        if (!PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_MUTE, true).isEmpty()) {
                                            for (Punishment punishment : PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_MUTE, true)) {

                                                Punishment punishment1 = new Punishment(playerName, formattedUUID, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                                if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment1))
                                                    punishment1.create(true);

                                            }
                                        }

                                        if (!PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.BAN, true).isEmpty()) {

                                            for (Punishment punishment : PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.BAN, true)) {
                                                Punishment punishment1 = new Punishment(playerName, formattedUUID, punishment.getReason() + "\n&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                                if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment1))
                                                    punishment1.create(true);

                                            }
                                        }

                                        if (!PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_BAN, true).isEmpty()) {
                                            for (Punishment punishment : PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_BAN, true)) {
                                                Punishment punishment1 = new Punishment(playerName, formattedUUID, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                                if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment1))
                                                    punishment1.create(true);

                                            }
                                        }

                                    }
                                });

//                                FernCommands.getInstance().getLogger().info("Player is " + uuid + " and is going to get punishment from player " + e.getPlayer().getName());


                            }
                        }
                    }
                }

                if (!runnablesToDo.isEmpty()) {
                    ProxyServer.getInstance().getScheduler().runAsync(FernCommands.getInstance(), () -> {
                        for (Runnable runnable : runnablesToDo) {
                            runnable.run();
                        }
                        runnablesToDo.clear();
                    });

                }
            });
        }
    }

    @EventHandler
    public void onPunish(PunishmentEvent e) {
        Universal.getScheduler().runAsync(() -> {
            if (e.getPunishment().getType() == PunishmentType.KICK || e.getPunishment().getType() == PunishmentType.WARNING)
                return;


            String uuidPlayer = e.getPunishment().getUuid();


//        print("Player UUID " + uuidPlayer + " is " + name + " and is getting others punished.");

            Config<IPSaveValues> ipconfig = FileManager.getIpConfig();

            FileManager.configLoad(ipconfig);

            if (uuidPlayer != null) {

                List<String> players;
                List<String> ips = new ArrayList<>();


                players = new ArrayList<>();
                players.add(uuidPlayer);

                IPSaveValues values = ipconfig.getConfigData();

                for (String ipe : values.getPlayerMap().keySet()) {
                    List<UUID> playips = values.getPlayers(ipe);
                    //print("The players for ip " + ipe + " has uuids " + playips.toString());

                    if (playips != null && !playips.isEmpty()) {
                        //ips.add(ipe);
                        boolean keepgoing = true;
                        for (UUID pluuid : playips) {
                            if (keepgoing) {
                                //print("The ip " + ipe + " has player uuid " + uuidPlayer + " with name " + name);
                                if (pluuid.toString().replaceAll("-", "").equals(uuidPlayer)) {
                                    ips.add(ipe);
                                    keepgoing = false;
                                }
                            }
                        }
                    }
                }

                List<String> bannedPlayers = new ArrayList<>();
                bannedPlayers.add(uuidPlayer);

                for (String ipe : ips) {
                    List<UUID> playips = values.getPlayers(ipe);
                    if (playips != null) {
                        for (UUID pluuid : playips) {

                            String formattedUUID = pluuid.toString().replaceAll("-", "");
                            if (!players.contains(formattedUUID)) {
                                players.add(formattedUUID);
                                //                        print("Ip " + ipe + " has player " + pluuid + " being added to the list.");


                                if (!bannedPlayers.contains(formattedUUID)) {
                                    //                            print("The uuid is " + pluuid);
                                    String playername;
                                    if (ProxyServer.getInstance().getPlayer(pluuid) != null) {
                                        playername = ProxyServer.getInstance().getPlayer(pluuid).getName();
                                    } else {
                                        playername = UUIDFetcher.getName(formattedUUID);
                                    }

                                    //                            print("A uuid is " + pluuid + " and name is " + playername + " punishing now.");
                                    if (playername != null) {

                                        Punishment punishment = new Punishment(playername, formattedUUID, e.getPunishment().getReason() + " &6From an alt which is " + UUIDFetcher.getName(e.getPunishment().getUuid()), e.getPunishment().getOperator(), e.getPunishment().getType(), e.getPunishment().getStart(), e.getPunishment().getEnd(), e.getPunishment().getCalculation(), -1);

                                        if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment)) {
                                            punishment.create(true);
                                            bannedPlayers.add(formattedUUID);
                                        } else {
                                            bannedPlayers.add(formattedUUID);
                                        }
                                    }
                                }


                            }
                        }
                    }
                }


//            print("Players list is " + players.toString());

//            print("Banned players are " + bannedPlayers.toString());
            } else {
                FernCommands.getInstance().printInLog(this, "Failed to load ips. UUID Player is " + uuidPlayer);
            }
        });

    }


    public void print(Object log) {
        if (log == null) log = "null because the object was null";
        FernCommands.getInstance().printInLog(this, log);
    }
}
