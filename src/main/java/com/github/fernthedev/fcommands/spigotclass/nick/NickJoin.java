package com.github.fernthedev.fcommands.spigotclass.nick;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import com.github.fernthedev.fcommands.spigotclass.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NickJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(FernCommands.getHookManager().isEssentials() && DatabaseHandler.isConnected()) {
            String sql = "SELECT * FROM fern_nicks;";
            PreparedStatement stmt;
            try {
                stmt = DatabaseHandler.getConnection().prepareStatement(sql);
                ResultSet result = stmt.executeQuery();

                String uuid = e.getPlayer().getUniqueId().toString();
                String playerName = e.getPlayer().getName();

                String nick = null;
                while (result.next()) {
                    String uUID = result.getString("PLAYERUUID");
                    if (uUID.equals(uuid)) {
                        nick = result.getString("NICK");
                    }
                }
                if(nick == null) return;

                if(!NickReload.nicknames.get(uuid).equals(nick)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:nick " + playerName + " " + nick);
                    NickReload.nicknames.remove(uuid);
                    NickReload.nicknames.put(uuid,nick);
                }


            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

}
