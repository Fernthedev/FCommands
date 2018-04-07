package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class Title {
    public static void send(Player p, Integer fadeIn, Integer fadeOut, Integer stay, String text){
        try
        {
            Object chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].
                    getMethod("a", String.class).invoke(null, "{\"text\": \"" + text + "\"}");

            Constructor<?> titleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).
                    getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0],
                            getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).
                    getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, fadeIn, stay, fadeOut);

            sendPacket(p, packet);
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).
                    invoke(playerConnection, packet);
        }
        catch(Exception ex)
        {
            //Do something
        }
    }

    /**
     * Get NMS class using reflection
     * @param name Name of the class
     * @return Class
     */
    private static Class<?> getNMSClass(String name)
    {
        try
        {
            return Class.forName("net.minecraft.server" + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
