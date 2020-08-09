package com.github.fernthedev.fcommands.proxy.data;

import com.github.fernthedev.fcommands.proxy.data.ip.IPDeletePlayerValue;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Data
@Getter
public class IPDeleteValues {

    //192 168 0 17:
    //  isToDelete: true
    //  Deleted: false
    //  Requested: f99a5767-0aae-48ca-a8a8-6b56e6a8c470
    //  HideDelete: '2019-09-24 13:30:18'
    //  Delete: '2019-10-01 12:30:18'

    private Map<String, IPDeletePlayerValue> playerDataValues = new HashMap<>();

    @Nullable
    public IPDeletePlayerValue getPlayerValue(@NonNull String ip) {
        return playerDataValues.get(ip);
     }

    public void removePlayerValue(@NonNull String ip) {
        playerDataValues.remove(ip);
    }


    public void addPlayer(String ip, IPDeletePlayerValue value) {
        playerDataValues.put(ip, value);
    }
}
