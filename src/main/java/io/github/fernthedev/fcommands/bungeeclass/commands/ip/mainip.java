package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class mainip extends Command {

    public mainip() {
        super("myip", "fernc.myip", "me");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            ShowIp.getInstance().execute(sender,args);
        }else{
            switch (args[0].toLowerCase()) {
                case "delete":
                    deleteip.getInstance().execute(sender,args);
                    break;
                    default:
                        sender.sendMessage(FernCommands.getInstance().message("&cNo such argument. (delete)"));
            }
        }
    }
}
