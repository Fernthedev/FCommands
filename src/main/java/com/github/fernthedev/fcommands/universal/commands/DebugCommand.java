package com.github.fernthedev.fcommands.universal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;

@CommandAlias("ferndebug")
@CommandPermission("fern.debug")
public class DebugCommand extends BaseCommand {

    /**
     * Called when executing the command
     *
     * @param sender The source
     */
    @Description("Toggle debug status")
    @Default
    public void onDebug(FernCommandIssuer sender) {
        Universal.setDebug(!Universal.isDebug());
        TextMessage message = TextMessage.fromColor("&aSet debug to " + (Universal.isDebug() ? ChatColor.GREEN : ChatColor.RED) + Universal.isDebug());
        sender.sendMessage(message);

        if (sender.isPlayer())
            Universal.getMethods().getLogger().info(message.toPlainText());
    }
}
