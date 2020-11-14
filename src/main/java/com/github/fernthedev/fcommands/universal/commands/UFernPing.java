package com.github.fernthedev.fcommands.universal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;

@CommandAlias("fernb|fping|fernbungee")
@CommandPermission("fern.ping")
public class UFernPing extends BaseCommand {


    @Default
    @CommandCompletion("* @nothing")
    @Syntax("[player]")
    public void execute(FernCommandIssuer sender, @Flags("other,defaultself") IFPlayer<?> p) {
        Universal.debug("Thing1 Start");

        if (sender.isPlayer() && (p == null || p.isPlayerNull() || p.getUniqueId() == sender.getUniqueId())) {
            Universal.debug("Thing send fern ping");
            sender.sendMessage(TextMessage.fromColor("&aYour FernPing is &9" + ((IFPlayer<?>) sender).getPing()));
        } else {
            Universal.debug("Thing1");

            Universal.debug("Player: " + p + " sender: " + sender);

            if (p == null || p.isPlayerNull() ) {
                Universal.debug("Player is null: " + p + " sender: " + sender);
                sender.sendError(MessageKeys.PLEASE_SPECIFY_ONE_OF, "{valid}", "Player");
                Universal.debug("Thing2");
                return;
            }

            if (p instanceof OfflineFPlayer<?> && !((OfflineFPlayer<?>) p).isOnline()) {
                sender.sendError(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", p.getName());
                Universal.debug("Thing3");
                return;
            }



            if (!sender.isPlayer() || sender.hasPermission("sv.see")) {
                Universal.debug("Thing4");
                sender.sendMessage(TextMessage.fromColor("&c" + p.getName() + "'s &aFernPing is " + p.getPing()));
            } else {
                Universal.debug("Thing5");
                if (p.isVanished()) {
                    Universal.debug("Thing6");
                    sender.sendError(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", p.getName());
//                    sender.sendMessage(TextMessage.fromColor("&cUnable to find &9" + p.getName()));
                } else {
                    Universal.debug("Thing7");
                    sender.sendMessage(TextMessage.fromColor("&c" + p + "'s &aFernPing is " + p.getPing()));
                }
            }
        }
    }
}
