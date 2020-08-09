package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.util.ProxyAskPlaceHolder;

@CommandAlias("bpapi")
@CommandPermission("fernc.bpapi")
public class GetPlaceholderCommand extends BaseCommand {



    @CommandCompletion("*")
    public void execute(CommandIssuer sender, IFPlayer<?> player, String placeHolder) {
        new ProxyAskPlaceHolder(player, placeHolder, (player1, placeHolder1, isReplaced) -> sender.sendMessage("&aThe player's placeholder value of " + placeHolder1 + " is " + placeHolder1));
    }

}
