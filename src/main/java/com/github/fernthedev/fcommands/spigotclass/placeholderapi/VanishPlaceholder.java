package com.github.fernthedev.fcommands.spigotclass.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class VanishPlaceholder extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "fvanish";
    }

    @Override
    public String getAuthor() {
        return "fernthedev";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.equalsIgnoreCase("isVanished")) {
            for (MetadataValue meta : p.getMetadata("vanished")) {
                if (meta.asBoolean()) return "Vanished";
            }
            return "Visible";
        }
        return null;
    }

}

