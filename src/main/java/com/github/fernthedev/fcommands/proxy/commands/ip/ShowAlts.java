package com.github.fernthedev.fcommands.proxy.commands.ip;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandAlias("accounts|acc")
@Description("Show alts of a player")
@CommandPermission("fernc.accounts")
public class ShowAlts extends BaseCommand {


    @Description("Show alts of a player")
    public void showAlts(FernCommandIssuer sender, @Optional IFPlayer<?> player) {
        String uuidPlayerCheck = player.isPlayerNull() ? UUIDFetcher.getUUID(player.getName()).toString() : player.getUuid().toString();

        if (uuidPlayerCheck == null) {
            sender.sendMessage(TextMessage.fromColor("&cUnable to find player."));
            return;
        }

//            String ip = null;
//
//
//            IFPlayer ifPlayer = Universal.getMethods().getPlayerFromName(plArgs);
//
//            if (!ifPlayer.isNull()) {
//                ip = ifPlayer.getAddress().getHostString().replaceAll("\\.", " ");
//            }


        uuidPlayerCheck = uuidPlayerCheck.replaceAll("-", "");

        Config<IPSaveValues> ipConfig = FileManager.getIpConfig();


        //String uuid = p.getUniqueId().toString();

        FileManager.configLoad(ipConfig);

//            List<String> players = new ArrayList<>();
        List<String> ips = new ArrayList<>();


//            players.add(uuidPlayerCheck);


        for (String ipKey : ipConfig.getConfigData().getPlayerMap().keySet()) {
            List<UUID> playerUUIDListFromIP = ipConfig.getConfigData().getPlayers(ipKey);
            //print("The players for ip " + ipKey + " has uuids " + playerUUIDListFromIP.toString());

            if (playerUUIDListFromIP != null && !playerUUIDListFromIP.isEmpty()) {
                //ips.add(ipKey);
                boolean keepgoing = true;
                for (UUID playerUUID : playerUUIDListFromIP) {
                    if (keepgoing && playerUUID.toString().replaceAll("-", "").equals(uuidPlayerCheck)) {
                        //print("The ip " + ipKey + " has player uuid " + uuidPlayerCheck + " with name " + name);

                        ips.add(ipKey);
                        keepgoing = false;

                    }
                }
            }
        }

        List<String> checkedPlayers = new ArrayList<>(); // Players we know are part of the alt list
        checkedPlayers.add(uuidPlayerCheck);
        sender.sendMessage(TextMessage.fromColor("&aSuccessfully found the player's alts. &bThe list is: "));

        Universal.debug("Found IPs " + ips);

        for (String ipFromList : ips) {

            List<UUID> uuidPlayersFromIP = ipConfig.getConfigData().getPlayers(ipFromList);

            Universal.debug("Checking for ip " + ipFromList + " the uuids " + uuidPlayersFromIP);

            if (uuidPlayersFromIP != null) {
                for (UUID uuid : uuidPlayersFromIP) {

                    if (checkedPlayers.contains(uuid.toString())) {
                        String playerName = UUIDFetcher.getName(uuid.toString().replaceAll("-", ""));
                        Universal.debug("Found that " + uuid + " is player " + playerName);
                        //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                        if (playerName == null) continue;

                        sender.sendMessage(TextMessage.fromColor("&3-&b" + playerName));
                        checkedPlayers.add(uuid.toString());
                    }
                }
            }
        }

        sender.sendMessage(TextMessage.fromColor("&6The ips are from: "));

        for (String ipString : ips) {
            sender.sendMessage(TextMessage.fromColor("&4-&c" + ipString));
        }

    }
}
