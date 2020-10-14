package com.github.fernthedev.fcommands.bungee.commands.ip;

import com.github.fernthedev.fcommands.bungee.FernCommands;
import com.github.fernthedev.fcommands.proxy.commands.ip.IPAlgorithms;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
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
            Universal.getScheduler().runAsync(() -> {

                List<Runnable> tasksTodo = new ArrayList<>();

                IPAlgorithms.IPUUIDLists ipData = IPAlgorithms.scan(e.getPlayer().getUniqueId());


                int runnablesPerThread = ipData.getIps().size() / 4;

                int runnablesDone = 0;

                for (UUID uuid : ipData.getUuids()) {

                    String formattedUUID = uuid.toString().replaceAll("-","");

                    runnablesDone++;

                    if (runnablesDone > runnablesPerThread) {
                        runnablesDone = 0;
                        List<Runnable> runnableList = new ArrayList<>(tasksTodo);
                        ProxyServer.getInstance().getScheduler().runAsync(FernCommands.getInstance(), () -> {
                            for (Runnable runnable : runnableList)
                                runnable.run();
                        });
                        tasksTodo.clear();
                    }

                    tasksTodo.add(() -> {
                        String playerName = UUIDFetcher.getName(formattedUUID);
                        if (PunishmentManager.get().isBanned(formattedUUID) || PunishmentManager.get().isMuted(formattedUUID)) {

                            List<Punishment> punishments = new ArrayList<>();

                            punishments.addAll(PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_MUTE, true));
                            punishments.addAll(PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.MUTE, true));

                            punishments.addAll(PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_BAN, true));
                            punishments.addAll(PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.IP_BAN, true));
                            punishments.addAll(PunishmentManager.get().getPunishments(formattedUUID, PunishmentType.TEMP_IP_BAN, true));


                            for (Punishment punishment : punishments) {
                                Punishment punishment1 = new Punishment(playerName, formattedUUID, punishment.getReason() + "&6From an alt which is " + UUIDFetcher.getName(punishment.getUuid()), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                                if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment1))
                                    punishment1.create(true);

                            }
                        }
                    });
                }

                if (!tasksTodo.isEmpty()) {
                    ProxyServer.getInstance().getScheduler().runAsync(FernCommands.getInstance(), () -> {
                        for (Runnable runnable : tasksTodo) {
                            runnable.run();
                        }
                        tasksTodo.clear();
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

            if (uuidPlayer != null) {


                IPAlgorithms.IPUUIDLists ipData = IPAlgorithms.scan(UUIDFetcher.uuidFromString(e.getPunishment().getUuid()));


                for (UUID plUuid : ipData.getUuids()) {
                    String playerName = UUIDFetcher.getName(plUuid);

                    //                            print("A uuid is " + plUuid + " and name is " + playerName + " punishing now.");
                    if (playerName != null) {

                        String formattedUUID = plUuid.toString().replace("-", "");

                        Punishment punishment = new Punishment(playerName, formattedUUID, e.getPunishment().getReason() + " &6From an alt which is " + UUIDFetcher.getName(e.getPunishment().getUuid()), e.getPunishment().getOperator(), e.getPunishment().getType(), e.getPunishment().getStart(), e.getPunishment().getEnd(), e.getPunishment().getCalculation(), -1);

                        if (!PunishmentManager.get().getPunishments(formattedUUID, punishment.getType(), true).contains(punishment)) {
                            punishment.create(true);
                        }
                    }
                }
            } else {
                FernCommands.getInstance().printInLog(this, "Failed to load ips. UUID Player is " + uuidPlayer);
            }
        });

    }
}
