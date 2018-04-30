package io.github.fernthedev.fcommands.spigotclass.ncp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings("unused")
public class cooldown {
    private final Map<UUID, expiration> cooldown = new HashMap<UUID, expiration>();

    /**
     * @param uuid to check
     * @return True if the Cooldown-Map contains the UUID
     */
    public boolean hasCooldown(UUID uuid) {
        return this.cooldown.containsKey(uuid);
    }

    /**
     * @param uuid to check
     * @return True if the cooldown has expired
     */
    public boolean isExpired(UUID uuid) {
        return this.cooldown.get(uuid).isExpired();
    }

    /**
     * Sets the Expiration of a cooldown
     *
     * @param uuid to insert
     * @param duration How long the cooldown should take effect
     */
    public void setExpiration(UUID uuid, duration duration) {
        this.cooldown.put(uuid, new expiration().expireIn(duration));
    }

    /**
     * Expire a cooldown now
     *
     * @param uuid of which's cooldown should expire
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
