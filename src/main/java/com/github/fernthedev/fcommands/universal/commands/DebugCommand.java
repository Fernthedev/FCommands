package com.github.fernthedev.fcommands.universal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;

import javax.inject.Inject;

@CommandAlias("ferndebug")
@CommandPermission("fern.debug")
public class DebugCommand extends BaseCommand {

    @Inject
    private APIHandler apiHandler;

    /**
     * Called when executing the command
     *
     * @param sender The source
     */
    @Description("Toggle debug status")
    @Default
    public void onDebug(FernCommandIssuer sender) {
        apiHandler.setDebug(!apiHandler.getDebug());
        TextMessage message = TextMessage.fromColor("&aSet debug to " + (apiHandler.getDebug() ? ChatColor.GREEN : ChatColor.RED) + apiHandler.getDebug());
        sender.sendMessage(message);

        if (sender.isPlayer()) {
            apiHandler.getMethods().getAbstractLogger().info(message.toPlainText());

        }
    }
}
