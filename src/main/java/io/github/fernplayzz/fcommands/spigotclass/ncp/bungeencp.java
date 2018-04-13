package io.github.fernplayzz.fcommands.spigotclass.ncp;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonSyntaxException;
import fr.neatmonster.nocheatplus.NCPAPIProvider;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.hooks.AbstractNCPHook;
import io.github.fernplayzz.fcommands.spigotclass.messaging;
import io.github.fernplayzz.fcommands.spigotclass.spigot;
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
public class bungeencp extends AbstractNCPHook implements PluginMessageListener,Listener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if (subChannel.equals("GetServer"))
            spigot.SERVER_NAME = in.readUTF();

        if (subChannel.equals(spigot.getInstance().getName())) {

            // Use the code sample in the 'Response' sections below to read
            // the data.
            byte[] msgBytes = new byte[in.readShort()];
            in.readFully(msgBytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            playerreport report = null;

            try {
                // Transform the JSON-String back to the PlayerReport class
                report = spigot.getGson().fromJson(msgIn.readUTF(), playerreport.class);
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
            }

            // Just in case if something went wrong during the transformation
            if (report == null)
                return;

            // Get the Report Message from the config and replace the variables
            String reportMessage = ChatColor.translateAlternateColorCodes('&', "&c&lNCP ( %server% ) &7»&r &c%player% &7could be using &6%check% &7VL &c%violations%");
            reportMessage = reportMessage.replaceAll("%player%", report.getPlayer()).replaceAll("%server%", report.getServer()).replaceAll("%check%", report.getCheckType().getName());

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

                if (spigot.SERVER_NAME.isEmpty() && spigot.getInstance().getServer().getPluginManager().getPlugin("LilyPad-Connect") == null)
                    messaging.sendRequest(event.getPlayer(), "GetServer");
            }
        }.runTaskLater(spigot.getInstance(), 5L);
        spigot.getInstance().checkForStaffMembers();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        spigot.getInstance().checkForStaffMembers();
        if (spigot.getCooldownManager().hasCooldown(player.getUniqueId()))
            spigot.getCooldownManager().removeCooldown(player.getUniqueId());
    }


    @Override
    public String getHookName() {
        return spigot.getInstance().getDescription().getName();
    }

    @Override
    public String getHookVersion() {
        return spigot.getInstance().getDescription().getVersion();
    }


    @Override
    public boolean onCheckFailure(CheckType checkType, Player player, IViolationInfo info) {

        if (spigot.getInstance().isStaffMemberOnline())
            return false;


        cooldown cooldown = spigot.getCooldownManager();

        // Player is still on cooldown, return false
        if (cooldown.hasCooldown(player.getUniqueId()) && !cooldown.isExpired(player.getUniqueId()))
            return false;

        try {
            // Send a report notification to other servers
            messaging.sendRequest(player, spigot.getGson().toJson(new playerreport(player.getName(), checkType, info.getTotalVl())), "Forward", "ONLINE", spigot.getInstance().getName());
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
