package com.github.fernthedev.fcommands.universal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("nh|fnh|namehistory")
@CommandPermission("fernc.namehistory")
public class NameHistory extends BaseCommand {

    private static HashMap<CommandIssuer, Long> cooldowns = new HashMap<>();

    public static final int DEFAULT_COOLDOWN = 60;


    public void execute(FernCommandIssuer sender, @Optional IFPlayer<?> player) {
        String playerName = player.getName();
        UUID uuid = player.getUuid();



        long secondsLeft = System.currentTimeMillis() - getCooldown(sender);

        if ((TimeUnit.MILLISECONDS.toSeconds(secondsLeft) <= DEFAULT_COOLDOWN)) {
            long timer = TimeUnit.MILLISECONDS.toSeconds(secondsLeft) - DEFAULT_COOLDOWN;
            sender.sendMessage(TextMessage.fromColor("You cant use that commands for another " + timer * -1 + " seconds!"));
            return;
        }


        if (!sender.hasPermission("fernc.namehistory.bypass")) {
            Universal.debug("Player does not have Cooldown bypass");
            setCooldown(sender, System.currentTimeMillis());

        }
//                else FernCommands.getInstance().getLogger().info("Player has Cooldown bypass, skipping.");

        if (uuid == null) {

        }


        List<UUIDFetcher.PlayerHistory> names = UUIDFetcher.getNameHistory(player.getUuid());
        if (names != null) {
            sender.sendMessage(TextMessage.fromColor("&b" + playerName + "'s names."));

            for (UUIDFetcher.PlayerHistory playerHistory : names) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String time = format.format(playerHistory.getTime());
                sender.sendMessage(TextMessage.fromColor("&b-&3" + playerHistory.getName()));
                if (playerHistory.getTimeDateInt() != 0)
                    sender.sendMessage(TextMessage.fromColor("&3  -&b" + time));
            }


        } else {
            sender.sendMessage(TextMessage.fromColor("&cUnable to find player history"));
        }


    }

    public void setCooldown(CommandIssuer player, long time){
        if(time < 1) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, time);
        }
    }

    public long getCooldown(CommandIssuer player){
        return cooldowns.getOrDefault(player, (long) 0);
    }

    protected static Iterable<String> returnPlayerComplete(String curText) {
        List<String> list = new ArrayList<>();

        for(IFPlayer<?> player : Universal.getMethods().getPlayers()) {
            if(player.getName().toLowerCase().contains(curText.toLowerCase())) {
                list.add(player.getName());
            }
        }

        return list;
    }
}
