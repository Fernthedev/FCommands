package com.github.fernthedev.fcommands.proxy.data;


import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class SeenValues {

    //f99a57670aae48caa8a86b56e6a8c470:
    //  time: Sep-2019 24 13.33
    //  server: survival

    private Map<UUID, SeenPlayerValue> playerValueMap = new HashMap<>();

    @Nullable
    public SeenPlayerValue getPlayers(UUID uuid) {

        for (UUID key : playerValueMap.keySet()) {
            if (key.equals(uuid)) return playerValueMap.get(key);
        }

        return null;
    }

    @Nullable
    public SeenPlayerValue getPlayers(String uuid) {

        for (UUID key : playerValueMap.keySet()) {
            if (key.toString().replaceAll("-","").equals(uuid.replace("-",""))) return playerValueMap.get(key);
        }

        return null;
    }
}
