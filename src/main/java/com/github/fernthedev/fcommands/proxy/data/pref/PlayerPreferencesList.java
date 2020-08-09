package com.github.fernthedev.fcommands.proxy.data.pref;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@ToString
public class PlayerPreferencesList {

    private Map<UUID, PlayerPreferencesSingleton> playerMap = new HashMap<>();


}
