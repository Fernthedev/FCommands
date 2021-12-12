package com.github.fernthedev.fcommands.spigot.ncp;

import com.github.fernthedev.fcommands.spigot.misc.Messaging;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import fr.neatmonster.nocheatplus.NCPAPIProvider;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.hooks.AbstractNCPHook;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


@Singleton
public class BungeeNCP extends AbstractNCPHook implements PluginMessageListener, Listener {

    private String serverName;
    private Cooldown cooldown = new Cooldown();

    private boolean isStaffMemberOnline = false;

    @Inject
    private Server server;

    @Inject
    private Plugin plugin;

    private static final Gson gson = new Gson();

    public void checkForStaffMembers() {
        for (Player onlineplayer : server.getOnlinePlayers()) {
            if (onlineplayer.hasPermission("nocheatplus.notify")) {
                isStaffMemberOnline = true;
                break;
            }
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if (subChannel.equals("GetServer"))
            serverName = in.readUTF();

        if (subChannel.equals(plugin.getName())) {

            // Use the code sample in the 'Response' sections below to read
            // the data.
            byte[] msgBytes = new byte[in.readShort()];
            in.readFully(msgBytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            PlayerReport report = null;

            try {
                // Transform the JSON-String back to the PlayerReport class
                report = gson.fromJson(msgIn.readUTF(), PlayerReport.class);
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
            reportMessage = reportMessage
                    .replace("%player%", report.getPlayer())
                    .replace("%server%", report.getServer())
                    .replace("%check%", report.getCheckType().getName())
                    .replace("%violation%", String.valueOf(report.getViolation()));

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
        }.runTaskLater(plugin, 5L);
        checkForStaffMembers();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        checkForStaffMembers();
        if (cooldown.hasCooldown(player.getUniqueId()))
            cooldown.removeCooldown(player.getUniqueId());
    }


    @Override
    public String getHookName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getHookVersion() {
        return plugin.getDescription().getVersion();
    }


    @Override
    public boolean onCheckFailure(CheckType checkType, Player player, IViolationInfo info) {
        if (isStaffMemberOnline)
            return false;

        // Player is still on Cooldown, return false
        if (cooldown.hasCooldown(player.getUniqueId()) && !cooldown.isExpired(player.getUniqueId()))
            return false;

        try {
            // Send a report notification to other servers
            Messaging.sendRequest(player, gson.toJson(new PlayerReport(player.getName(), checkType, info.getTotalVl(), serverName)), "Forward", "ONLINE", plugin.getName());
        } catch (IOException e) {

        }

        return true;
    }

}
