package com.github.fernthedev.fcommands.proxy.data;


import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Getter
public class IPSaveValues {

    private Map<String, List<UUID>> playerMap = new HashMap<>();

    public void removePlayer(String ip) {
        for (String key : playerMap.keySet()) {
            if (key.equals(ip)) playerMap.remove(key);
        }
    }

    @Nullable
    public List<UUID> getPlayers(String ip) {

        for (String key : playerMap.keySet()) {
            if (key.equals(ip)) return playerMap.get(key);
        }

        return null;
    }
}
