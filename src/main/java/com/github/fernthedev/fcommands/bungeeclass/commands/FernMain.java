package com.github.fernthedev.fcommands.bungeeclass.commands;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class FernMain extends Command {

    private static FernMain ourInstance = new FernMain();

    public static FernMain getInstance() {
        return ourInstance;
    }

    public FernMain() {
        super("fernc", "fernc.Help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
                sender.sendMessage(new FernCommands().message("&9Hello there. FernCommands are running"));
        }else{
            if(args[0].equalsIgnoreCase("reload")) {
                ReloadConfig.getInstance().execute(sender,args);
            }else{
                sender.sendMessage(new FernCommands().message("&cWrong arguments recieved "+ "\"" + args[0] + "\"" + " is not valid. (reload)" ));
            }
        }
    }

    public static void onDisable() {
        ourInstance = null;
    }
}
