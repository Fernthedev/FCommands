package com.github.fernthedev.fcommands.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;

import java.io.IOException;

@CommandAlias("fernc")
public class FernMain extends BaseCommand {

//    public FernMain() {
//        super("fernc", "fernc.Help");
//
////        ArgumentRunnable argumentRunnable = (sender, args) -> {
////            String which = args[0].toLowerCase();
////            if (which.equals("all") || which.equals("ip") || which.equals("config") || which.equals("seen")) {
////                try {
////                    FileManager.getInstance().loadFiles(which,false);
////                }catch (IOException e) {
////                    Universal.getMethods().getLogger().warning("&cUnable to reload files");
////                    sender.sendMessage(message("&cUnable to reload files"));
////                }
////
////                Universal.getMethods().getLogger().info("Successfully reloaded files");
////                sender.sendMessage(message("&aSuccessfully reloaded files"));
////            } else {
////                sender.sendMessage(message("&cWrong arguments received " + "\"" + which + "\"" + " (All,Config,Ip)"));
////            }
////        };
//
//        addArgument(new Argument("reload", null,
//                configArgument("all"),
//                configArgument("ip"),
//                configArgument("config"),
//                configArgument("seen")
//        ));
//    }

    private static final String configList = "config reload|all|ip|config";

    @Description("Reload configs")
    @CommandCompletion(configList)
    @Subcommand("config")
    @CommandPermission("fernc.config.reload")
    public void reloadConfig(CommandIssuer sender, String config) throws IOException {
        try {
            FileManager.getInstance().loadFiles(config, false);
        }catch (IOException e) {
            Universal.getMethods().getLogger().warning("&cUnable to reload files");
            throw e;
//            sender.sendMessage(MessageType.ERROR, MessageKeys.);
//            sender.sendMessage(message("&cUnable to reload files"));
        }

        Universal.getMethods().getLogger().info("Successfully reloaded files");


        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded files");
    }

    @Description("Show help data")
    @CommandPermission("fernc.help")
    @HelpCommand
    @Default
    public void onHelp(CommandIssuer sender, CommandHelp help) {
        sender.sendMessage(ChatColor.BLUE + "Hello there. FernCommands are running. " + Universal.getPlugin().getPluginData().getVersion());
        help.showHelp();
    }

//    @Override
//    public void execute(CommandIssuer sender, String[] args) {
//        if(args.length == 0) {
//                sender.sendMessage(message("&9Hello there. FernCommands are running. "));
//        }else{
//            try {
//                Argument argument = handleArguments(sender, args[0]);
//                argument.getArgumentRunnable().run(sender, Arrays.copyOfRange(args, 1, args.length));
//            } catch (ArgumentNotFoundException e) {
//
//                String parentArg = e.getArgumentInfo().getParentArgument() == null ? null : e.getArgumentInfo().getParentArgument().getName();
//
//                sender.sendMessage(message("&cWrong arguments received \""
//                        + e.getArgumentInfo().getProvidedArg() + "\" is not valid " + (
//                                parentArg != null ? ("for " + parentArg) : ""
//                        ) + ". "
//                        + e.getArgumentInfo().getPossibleArguments().stream().map(Argument::getName).collect(Collectors.toList()))
//                );
//            }
//        }
//    }
}
