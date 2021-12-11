package com.github.fernthedev.fcommands.bungee.commands.ip;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.bungee.FernCommands;
import com.github.fernthedev.fcommands.proxy.ProxyFileManager;
import com.github.fernthedev.fcommands.proxy.WhichFile;
import com.github.fernthedev.fcommands.proxy.commands.ip.IPAlgorithms;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.proxy.modules.ProxyFile;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AltsBan implements Listener {

    @ProxyFile(WhichFile.CONFIG)
    @Inject
    private Config<ConfigValues> config;

    @Inject
    private ProxyFileManager proxyFileManager;

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        if (FernCommands.getHookManager().hasAdvancedBan() && config.getConfigData().getAltsBan()) {
            Universal.getScheduler().runAsync(() -> {

                String formattedUUIDJoin = e.getPlayer().getUniqueId().toString().replace("-","");

                if (PunishmentManager.get().isBanned(formattedUUIDJoin))
                    return;

                List<Runnable> tasksTodo = new ArrayList<>();

                IPAlgorithms.IPUUIDLists ipData = IPAlgorithms.scan(e.getPlayer().getUniqueId(), proxyFileManager);


                int tasksPerThread = ipData.getIps().size() / 4;

                int tasksQueued = 0;

                for (UUID uuid : ipData.getUuids()) {

                    String formattedUUID = uuid.toString().replace("-","");

                    tasksQueued++;

                    if (tasksQueued > tasksPerThread) {
                        tasksQueued = 0;
                        List<Runnable> runnableList = new ArrayList<>(tasksTodo);
                        Universal.getScheduler().runAsync(() -> {
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

                                boolean hasPunishmentAlready = hasPunishmentAlready(formattedUUID, punishment1);

                                if (!hasPunishmentAlready) {
                                    Universal.debug("Punishing because not punished already " + punishment1);
                                    punishment1.create(true);
                                }
                            }
                        }
                    });
                }

                if (!tasksTodo.isEmpty()) {
                    Universal.getScheduler().runAsync(() -> {
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
        if (!config.getConfigData().getAltsBan()) return;


        if (e.getPunishment().getType() == PunishmentType.KICK || e.getPunishment().getType() == PunishmentType.WARNING)
            return;


        String uuidPlayer = e.getPunishment().getUuid();

        Universal.getScheduler().runAsync(() -> {
            if (uuidPlayer != null) {


                IPAlgorithms.IPUUIDLists ipData = IPAlgorithms.scan(UUIDFetcher.uuidFromString(e.getPunishment().getUuid()), proxyFileManager);


                for (UUID plUuid : ipData.getUuids()) {
                    String playerName = UUIDFetcher.getName(plUuid);

                    //                            print("A uuid is " + plUuid + " and name is " + playerName + " punishing now.");
                    if (playerName != null) {

                        String formattedUUID = plUuid.toString().replace("-", "");

                        Punishment punishment = new Punishment(playerName, formattedUUID, e.getPunishment().getReason() + " &6From an alt which is " + UUIDFetcher.getName(e.getPunishment().getUuid()), e.getPunishment().getOperator(), e.getPunishment().getType(), e.getPunishment().getStart(), e.getPunishment().getEnd(), e.getPunishment().getCalculation(), -1);

                        boolean hasPunishmentAlready = hasPunishmentAlready(formattedUUID, punishment);


                        if (!hasPunishmentAlready) {
                            Universal.debug("Punishing because not punished already " + punishment);
                            punishment.create(true);
                        }
                    }
                }
            } else {
                Universal.getLogger().error("Failed to load ips. UUID Player is " + null);
            }
        });

    }

    private boolean hasPunishmentAlready(String formattedUUID, Punishment checkPunishment) {
        for (Punishment punishment1 : PunishmentManager.get().getPunishments(formattedUUID, checkPunishment.getType(), true)) {

            Universal.debug("Checking punishments: \ncheck" + punishment1.toString() + "\nparam: " + checkPunishment);
            if (
                    punishment1.getUuid().equals(checkPunishment.getUuid()) &&
                            punishment1.getEnd() == checkPunishment.getEnd() &&
                            punishment1.getStart() == checkPunishment.getStart() &&
                            punishment1.getOperator().equals(checkPunishment.getOperator()) &&
                            punishment1.getName().equals(checkPunishment.getName())
            ) {
                return true;
            }
        }

        return false;
    }
}
