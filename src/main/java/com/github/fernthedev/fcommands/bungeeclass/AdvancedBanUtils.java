package com.github.fernthedev.fcommands.bungeeclass;

import lombok.Synchronized;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;

import java.util.List;

public class AdvancedBanUtils {


    @Synchronized
    public static List<Punishment> getPunishments(String uuid, PunishmentType put, boolean current) {
        return PunishmentManager.get().getPunishments(uuid, put, current);
    }

    public static boolean isBannedOrMuted(String formattedUUID) {
        return isBanned(formattedUUID) || isMuted(formattedUUID);
    }

    @Synchronized
    private static boolean isMuted(String formattedUUID) {
        return PunishmentManager.get().isMuted(formattedUUID);
    }

    @Synchronized
    public static boolean isBanned(String uuid) {
        return PunishmentManager.get().isBanned(uuid);
    }

    @Synchronized
    public static Punishment getBan(String replaceAll) {
        return PunishmentManager.get().getBan(replaceAll);
    }
}
