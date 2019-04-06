package com.github.fernthedev.fcommands.bungeeclass.commands;

import com.github.fernthedev.fernapi.server.bungee.FernCommand;
import com.github.fernthedev.fernapi.server.bungee.network.AskPlaceHolder;
import com.github.fernthedev.fernapi.server.bungee.network.MessageRunnable;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GetPlaceholderCommand extends FernCommand {


    public GetPlaceholderCommand(String name, String permission, String... aliases) {
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
                    System.out.println("Called");
                    super.run();
                    sender.sendMessage(message("&aThe player's placeholder value of " + args[1] + " is " + askPlaceHolder.getPlaceHolderResult()));
                }
            });
        }else{
            sender.sendMessage(message("&cInsufficient arguments. Usage: bpapi {player} {placeholder}"));
        }
    }

}
