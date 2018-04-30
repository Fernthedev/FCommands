package io.github.fernthedev.fcommands.spigotclass;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NTEmcMMO implements Listener {

    @SuppressWarnings("privatation")
    public static void ntereload() {
        Bukkit.getServer().getLogger().info("Reloading nte due to levelup from mcmmo");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"nte reload");
    }
    //@EventHandler
   // public void levelup(McMMOPlayerLevelUpEvent upe) {
   //     ntereload();
   // }
    @EventHandler
    public static void onLevelChange(final McMMOPlayerLevelUpEvent change) {
        Bukkit.getServer().getLogger().info("Player leveled up, reloading nametags");
        ntereload();
    }


}
