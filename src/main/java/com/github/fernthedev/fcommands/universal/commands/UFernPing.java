package com.github.fernthedev.fcommands.universal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;

@CommandAlias("fernb|fping|fernbungee|ping")
@CommandPermission("fern.ping")
public class UFernPing extends BaseCommand {



    @Default
    @CommandCompletion("* @nothing")
    @Syntax("[player]")
    public void execute(FernCommandIssuer sender, @Flags("other,defaultself") IFPlayer<?> p) {
        APIHandler.debug(() -> "Thing1 Start");

        if (sender.isPlayer() && (p == null || p.isPlayerNull() || p.getUniqueId() == sender.getUniqueId())) {
            APIHandler.debug(() -> "Thing send fern ping");
            sender.sendMessage(TextMessage.fromColor("&aYour FernPing is &9" + ((IFPlayer<?>) sender).getPing()));
        } else {
            APIHandler.debug(() -> "Thing1");

            APIHandler.debug(() -> "Player: " + p + " sender: " + sender);

            if (p == null || p.isPlayerNull() ) {
                APIHandler.debug(() -> "Player is null: " + p + " sender: " + sender);
                sender.sendError(MessageKeys.PLEASE_SPECIFY_ONE_OF, "{valid}", "Player");
                APIHandler.debug(() -> "Thing2");
                return;
            }

            if (p instanceof OfflineFPlayer<?> && !((OfflineFPlayer<?>) p).isOnline()) {
                sender.sendError(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", p.getName());
                APIHandler.debug(() -> "Thing3");
                return;
            }



            if (!sender.isPlayer() || sender.hasPermission("sv.see")) {
                APIHandler.debug(() -> "Thing4");
                sender.sendMessage(TextMessage.fromColor("&c" + p.getName() + "'s &aFernPing is " + p.getPing()));
            } else {
                APIHandler.debug(() -> "Thing5");
                p.isVanished().thenAccept(vanished -> {
                    if (Boolean.TRUE.equals(vanished)) {
                        APIHandler.debug(() -> "Thing6");
                        sender.sendError(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", p.getName());
//                    sender.sendMessage(TextMessage.fromColor("&cUnable to find &9" + p.getName()));
                    } else {
                        APIHandler.debug(() -> "Thing7");
                        sender.sendMessage(TextMessage.fromColor("&c" + p.getName() + "'s &aFernPing is " + p.getPing()));
                    }
                });
            }
        }
    }
}
