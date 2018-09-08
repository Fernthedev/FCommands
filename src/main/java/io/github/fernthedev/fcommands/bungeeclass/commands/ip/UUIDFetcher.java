package io.github.fernthedev.fcommands.bungeeclass.commands.ip;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class UUIDFetcher {

    private UUIDFetcher() {}

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static HashMap<UUID, String> names = new HashMap<>();
    private static HashMap<String, UUID> uuids = new HashMap<>();

    public static UUID getUUID(ProxiedPlayer p) {
        return getUUID(p.getName());
    }

    public static UUID getUUID(String name) {
        if (name == null)
            return UUID.randomUUID();
        name = name.toLowerCase();

        if (uuids.containsKey(name))
            return uuids.get(name);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(UUID_URL, name, System.currentTimeMillis() / 1000)).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID player = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())),
                    PlayerUUID.class);

            uuids.put(name, player.getId());

            return player.getId();
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger()
                    .info("Your server has no connection to the mojang servers or is runnig slowly.");
            uuids.put(name, ProxyServer.getInstance().getPlayer(name).getUniqueId());
            return ProxyServer.getInstance().getPlayer(name).getUniqueId();
        }
    }

    public static String getName(UUID uuid) {

        if (names.containsKey(uuid))
            return names.get(uuid);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID[] allUserNames = gson.fromJson(
                    new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID[].class);
            PlayerUUID currentName = allUserNames[allUserNames.length - 1];

            names.put(uuid, currentName.getName());

            return currentName.getName();
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().info("§cYour server has no connection to the mojang servers or is runnig slow.");
            names.put(uuid, ProxyServer.getInstance().getPlayer(uuid).getName());
            return ProxyServer.getInstance().getPlayer(uuid).getName();
        }
    }
}

class PlayerUUID {

    private String name;
    private UUID id;

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

}
