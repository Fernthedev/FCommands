package io.github.fernthedev.fcommands.bungeeclass.commands;

import io.github.fernthedev.fcommands.bungeeclass.bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class fernmain extends Command {

    private static fernmain ourInstance = new fernmain();

    public static fernmain getInstance() {
        return ourInstance;
    }

    public fernmain() {
        super("fernc", "fernc.help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
                sender.sendMessage(new bungee().message("&9Hello there. FernCommands are running"));
        }else{
            if(args[0].equalsIgnoreCase("reload")) {
                reloadconfig.getInstance().execute(sender,args);
            }else{
                sender.sendMessage(new bungee().message("&cWrong arguments recieved "+ "\"" + args[0] + "\"" + " is not valid. (reload)" ));
            }
        }
    }
}
