package io.github.fernthedev.fcommands.bungeeclass.commands;

import io.github.fernthedev.fcommands.bungeeclass.MessageRunnable;
import io.github.fernthedev.fcommands.bungeeclass.placeholderapi.AskPlaceHolder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class getPlaceholderCommand extends FCommand {


    public getPlaceholderCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 1) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);

            if(player == null) {
                sendMessage(sender,"&cThe player &4" + args[0] + " &ccould not be found.");
                return;
            }

            AskPlaceHolder askPlaceHolder = new AskPlaceHolder(player,args[1]);

            askPlaceHolder.setRunnable(new MessageRunnable() {
                @Override
                public void run() {
                    super.run();
                    sender.sendMessage(msg("&aThe player's placeholder value of " + args[1] + " is " + askPlaceHolder.getPlaceHolderResult()));
                }
            });
        }else{
            sender.sendMessage(msg("&cInsufficient arguments. Usage: bpapi {player} {placeholder}"));
        }
    }

}
