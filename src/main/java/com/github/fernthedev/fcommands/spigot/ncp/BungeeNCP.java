package com.github.fernthedev.fcommands.spigot.ncp;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.misc.Messaging;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonSyntaxException;
import fr.neatmonster.nocheatplus.NCPAPIProvider;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.hooks.AbstractNCPHook;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


@SuppressWarnings("CatchMayIgnoreException")
public class BungeeNCP extends AbstractNCPHook implements PluginMessageListener,Listener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if (subChannel.equals("GetServer"))
            FernCommands.SERVER_NAME = in.readUTF();

        if (subChannel.equals(FernCommands.getInstance().getName())) {

            // Use the code sample in the 'Response' sections below to read
            // the data.
            byte[] msgBytes = new byte[in.readShort()];
            in.readFully(msgBytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            PlayerReport report = null;

            try {
                // Transform the JSON-String back to the PlayerReport class
                report = FernCommands.getGson().fromJson(msgIn.readUTF(), PlayerReport.class);
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
            }

            // Just in case if something went wrong during the transformation
            if (report == null)
                return;

            String violation;
            if(report.getViolation() <= 10) {
                violation = "&a%violation%";
            } else if(report.getViolation() >10 && report.getViolation() <= 20) {
                violation = "&6%violation%";
            }else{
                violation = "&c%violation%";
            }
            // Get the Report Message from the config and replace the variables
            String reportMessage = ChatColor.translateAlternateColorCodes('&', "&c&lNCP ( %server% ) &7»&r &c%player% &7could be using &6%check% &7VL " + violation);
            reportMessage = reportMessage.replaceAll("%player%", report.getPlayer()).replaceAll("%server%", report.getServer()).replaceAll("%check%", report.getCheckType().getName()).replaceAll("%violation%", String.valueOf(report.getViolation()));

            // Send an admin notification to players with the corresponding permission (only if the player has turned notifications on)
            NCPAPIProvider.getNoCheatPlusAPI().sendAdminNotifyMessage(reportMessage);
        }
        }
        @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Fetch the server's name from BungeeCord because Plugin Messaging Channels require a player who sends the message.
        // It seems like it doesn't work without a runnable.
        new BukkitRunnable() {
            @Override
            public void run() {

                    Messaging.sendRequest(event.getPlayer(), "GetServer");
            }
        }.runTaskLater(FernCommands.getInstance(), 5L);
        FernCommands.getInstance().checkForStaffMembers();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FernCommands.getInstance().checkForStaffMembers();
        if (FernCommands.getCooldownManager().hasCooldown(player.getUniqueId()))
            FernCommands.getCooldownManager().removeCooldown(player.getUniqueId());
    }


    @Override
    public String getHookName() {
        return FernCommands.getInstance().getDescription().getName();
    }

    @Override
    public String getHookVersion() {
        return FernCommands.getInstance().getDescription().getVersion();
    }


    @Override
    public boolean onCheckFailure(CheckType checkType, Player player, IViolationInfo info) {

        if (FernCommands.getInstance().isStaffMemberOnline())
            return false;


        Cooldown cooldown = FernCommands.getCooldownManager();

        // Player is still on Cooldown, return false
        if (cooldown.hasCooldown(player.getUniqueId()) && !cooldown.isExpired(player.getUniqueId()))
            return false;

        try {
            // Send a report notification to other servers
            Messaging.sendRequest(player, FernCommands.getGson().toJson(new PlayerReport(player.getName(), checkType, info.getTotalVl())), "Forward", "ONLINE", FernCommands.getInstance().getName());
        } catch (IOException e) {

        }
        /*try {
            // Send a report notification to other servers
            Request.sendRequest(player, BungeeNCPNotify.getGson().toJson(new PlayerReport(player.getName(), checkType, info.getTotalVl())), "Forward", "ONLINE", BungeeNCPNotify.getInstance().getName());
        } catch (IOException e) {
        }*/


        return true;
    }

}
