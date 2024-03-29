package com.github.fernthedev.fcommands.spigot.ncp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings("unused")
public class Cooldown {
    private final Map<UUID, Expiration> cooldown = new HashMap<>();

    /**
     * @param uuid to check
     * @return True if the Cooldown-Map contains the UUID
     */
    public boolean hasCooldown(UUID uuid) {
        return this.cooldown.containsKey(uuid);
    }

    /**
     * @param uuid to check
     * @return True if the Cooldown has expired
     */
    public boolean isExpired(UUID uuid) {
        return this.cooldown.get(uuid).isExpired();
    }

    /**
     * Sets the Expiration of a Cooldown
     *
     * @param uuid to insert
     * @param duration How long the Cooldown should take effect
     */
    public void setExpiration(UUID uuid, Duration duration) {
        this.cooldown.put(uuid, new Expiration().expireIn(duration));
    }

    /**
     * Expire a Cooldown now
     *
     * @param uuid of which's Cooldown should expire
     */
    public void expireNow(UUID uuid) {
        this.cooldown.get(uuid).expireNow();
    }

    /**
     * Remove a UUID from the Cooldown-Map
     *
     * @param uuid to remove
     */
    public void removeCooldown(UUID uuid) {
        this.cooldown.remove(uuid);
    }
}
