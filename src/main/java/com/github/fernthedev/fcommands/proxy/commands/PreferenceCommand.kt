package com.github.fernthedev.fcommands.proxy.commands

import co.aikar.commands.*
import co.aikar.commands.annotation.*
import com.github.fernthedev.fcommands.proxy.FileManager
import com.github.fernthedev.fcommands.proxy.data.pref.PrefDataParameter
import com.github.fernthedev.fcommands.proxy.data.pref.PreferenceDataAbstract
import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer
import com.github.fernthedev.fernapi.universal.api.IFPlayer
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor
import com.github.fernthedev.fernapi.universal.data.chat.ClickData
import com.github.fernthedev.fernapi.universal.data.chat.HoverData
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage
import java.lang.reflect.Field
import java.util.Optional
import java.util.stream.Collectors


@CommandPermission("fernc.preference")
@CommandAlias(PreferenceCommand.COMMAND_NAME + "|preference")
class PreferenceCommand : BaseCommand() {

    init {

        val manager = Universal.getCommandHandler()

        manager.getCommandCompletions().registerAsyncCompletion("prefList") { context: CommandCompletionContext<*> ->
            if (!context.issuer.isPlayer)
                emptyList<String>()
            else {
                val pref = FileManager.getPlayerPref(context.issuer.uniqueId)

                pref.possibleFields
                        .parallelStream()
                        .map { p -> pref.getFromString(p.name).name }
                        .collect(Collectors.toList())
            }
        }

        manager.getCommandContexts().registerContext(PreferenceDataAbstract::class.java) { commandExecutionContext ->
            (if (!commandExecutionContext.issuer.isPlayer)
                throw InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false)
            else {
                val arg = commandExecutionContext.popFirstArg();

                val pref = FileManager.getPlayerPref(commandExecutionContext.issuer.uniqueId)
                val f: Optional<Field> = pref!!.possibleFields
                        .parallelStream()
                        .filter { p -> pref.getFromString(p.name).name == arg }
                        .findAny()

                if (f.isPresent) {
                    pref.getFromString(f.get().name)
                } else {
                    throw InvalidCommandArgument(MessageKeys.NO_COMMAND_MATCHED_SEARCH, false, "{search}", arg)
                }

            })
        }

        manager.getCommandCompletions().setDefaultCompletion("prefList", PreferenceDataAbstract::class.java)




        // TODO: make infer based on previous argument
        manager.getCommandCompletions().registerAsyncCompletion("prefValueTypes") { context: CommandCompletionContext<*> ->
            if (!context.issuer.isPlayer)
                emptyList<String>()
            else {
                val pref = context.getContextValue(PreferenceDataAbstract::class.java)
                        ?: throw InvalidCommandArgument("Preference ${context.input} not found", false);

                pref.possibleValuesString()
            }
        }
        manager.getCommandCompletions().setDefaultCompletion("prefValueTypes", PrefDataParameter::class.java)

        manager.getCommandContexts().registerContext(PrefDataParameter::class.java) { commandExecutionContext ->
            (if (!(commandExecutionContext.issuer as CommandIssuer).isPlayer)
                throw InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false)
            else {


                //                    val pref = FileManager.getPlayerPref(commandExecutionContext.issuer.uniqueId)
                //                    val prefDataParameter: PreferenceDataAbstract<*> = pref!!.possibleFields
                //                            .parallelStream()
                //                            .filter { p -> p.name == commandExecutionContext.popFirstArg() }
                //                            .findAny().get().get(pref) as PreferenceDataAbstract<*>
                val arg = commandExecutionContext.popFirstArg();

                val pref = FileManager.getPlayerPref(commandExecutionContext.issuer.uniqueId)

                val f: Optional<Field> = pref!!.possibleFields
                        .parallelStream()
                        .filter { p -> pref.getFromString(p.name).name == arg }
                        .findAny()

                if (f.isPresent) {
                    val prefData = pref.getFromString(f.get().name)

                    PrefDataParameter(prefData.parse(commandExecutionContext.popFirstArg()));
                } else {
                    throw InvalidCommandArgument(MessageKeys.NO_COMMAND_MATCHED_SEARCH, false, "{search}", arg)
                }
            })
        }
//        manager.getCommandConditions().addCondition(PrefDataParameter::class.java, "prefDataParamCondition") { context, commandExecutionContext, value ->
//            if (value != null) {
//                val pref: PreferenceDataAbstract<*> = commandExecutionContext.getResolvedArg(PreferenceDataAbstract::class.java) as PreferenceDataAbstract<*>;
//
//                try {
//                    pref.isValid(commandExecutionContext)
//                } catch (e: java.lang.Exception) {
//                    throw InvalidCommandArgument(e.localizedMessage, true)
//                }
//            }
//        }
    }


    @Conditions("player")
    @Default
    fun listPreferences(sender: IFPlayer<Any>) {
        val pref = FileManager.getPlayerPref(sender.uniqueId)




        sender.sendMessage("${ChatColor.GREEN}Your preferences:")

        pref!!.possibleFields
                .parallelStream()
                .map { p -> pref.getFromString(p.name) as PreferenceDataAbstract<*>}
                .forEach {prefData ->
                    run {
                        sender.sendMessage(prefMessage(prefData))
                    }
                }



        //            sender.sendMessage(TextMessage.fromColor("&bUse 12 hour format: &2{12hour} &9" + pref.getHour12Format().getValue()));
//            TimeZone timeZone = TimeZone.getTimeZone(pref.getPreferredTimezone().getValue());
//
//            sender.sendMessage(TextMessage.fromColor("&bTimezone: &2{zone} &9" + timeZone.getDisplayName() + " {" + timeZone.getID() + "}"));
//        sender.sendMessage(prefMessage(pref.hour12Format))
//        sender.sendMessage(prefMessage(pref.preferredTimezone))
    }

//    @HelpCommand
//    fun onHelp(issuer: CommandIssuer, help: CommandHelp) {
//        issuer.sendMessage("${ChatColor.RED}Format: /$COMMAND_NAME {set} {data} {value}")
//    }


    @Subcommand("set")
    @CommandPermission("fernc.preferences.modify")
    @CommandCompletion("@prefList @prefValueTypes")
    fun <T> onSet(sender: FernCommandIssuer, /*@Values("@prefList")*/ prefName: PreferenceDataAbstract<T>, prefWrappedValue: PrefDataParameter<T>) {

        val value = prefWrappedValue.data;

        try {
            prefName.value = value

            sender.sendMessage(TextMessage.fromColor("&aSuccess updating value"))

            FileManager.configSave(FileManager.getPlayerPreferencesGsonConfig())
        } catch (e: java.lang.Exception) {
            sender.sendError(MessageKeys.ERROR_PREFIX, "{message}", e.localizedMessage)
        }

//
//        val pref = FileManager.getPlayerPref(sender.uniqueId)
//        if (pref.hour12Format.name.equals(prefName.name, ignoreCase = true)) {
//            if (!value!!.equals("false", ignoreCase = true) && !value.equals("true", ignoreCase = true)) {
//                sender.sendMessage(TextMessage.fromColor("Only true/false values allowed"))
//                return
//            }
//            pref.hour12Format.value = java.lang.Boolean.parseBoolean(value)
//            FileManager.configSave(FileManager.getPlayerPreferencesGsonConfig())
//            sender.sendMessage(TextMessage.fromColor("&aUpdated value"))
//        } else if (pref.preferredTimezone.name.equals(data, ignoreCase = true)) {
//            var failed = false
//            var zone: TimeZone? = null
//            try {
//                zone = TimeZone.getTimeZone(value)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                failed = true
//            }
//            if (zone == null || pref.preferredTimezone.isValid(zone)) failed = true
//            if (failed) {
//                sender.sendMessage(TextMessage.fromColor("&cUnable to parse time zone. Use example such as PST, \"America/Los_Angeles\" or GMT-8:00"))
//                return
//            }
//            pref.preferredTimezone.value = value
//            FileManager.configSave(FileManager.getPlayerPreferencesGsonConfig())
//            sender.sendMessage(TextMessage.fromColor("&aUpdated value"))
//        } else {
//            sender.sendMessage(TextMessage.fromColor("&cUnable to find data {$data}"))
//        }
    }

    private fun prefMessage(preferenceData: PreferenceDataAbstract<*>): TextMessage {
        // "&bUse 12 hour format: &2{12hour} &9" + pref.getHour12Format().getValue();
        val textComponent = TextMessage()
        textComponent.addExtra(TextMessage.fromColor("&a" + preferenceData.name + " &2= &b{" + preferenceData.value + "}"))
        textComponent.clickData = ClickData(ClickData.Action.SUGGEST_COMMAND, "/" + COMMAND_NAME + " set " + preferenceData.name + " ")
        textComponent.hoverData = HoverData(HoverData.Action.SHOW_TEXT, TextMessage.fromColor("&9Modify value of &b" + preferenceData.name))
        return textComponent
    }

//    fun suggest(sender: CommandIssuer, args: Array<String>): List<String?> {
//        if (sender is IFPlayer<*>) {
//            val p = sender as IFPlayer<*>
//            var stringList: List<String?> = ArrayList()
//            val options: MutableList<String> = ArrayList()
//            val preferencesSingleton = FileManager.getPlayerPref(p.uuid)
//            if (args.size > 3 || args.size == 0) {
//                return ArrayList()
//            }
//            if (args.size == 1) {
//                options.add("help")
//                options.add("set")
//                stringList = search(args[0], options)
//            }
//            if (args.size == 2 && args[0].equals("set", ignoreCase = true)) {
//                options.add(preferencesSingleton.preferredTimezone.name)
//                options.add(preferencesSingleton.hour12Format.name)
//                stringList = search(args[1], options)
//            }
//            if (args.size == 3) {
//                if (args[1].equals(preferencesSingleton.preferredTimezone.name, ignoreCase = true)) {
//
////                zones.forEach(s -> {
////                    if(s.contains(args[1])) {
////                        stringList.add(s);
////                    }
////                });
//                    stringList = search(args[2], PreferenceDataTimezone.possibleValuesS())
//
////                    PreferenceDataTimezone.possibleValuesS().forEach(s -> {
////                        if(s.contains(args[2])) {
////                            stringList.add(s);
////                        }
////                    });
//                }
//                if (args[1].equals(preferencesSingleton.hour12Format.name, ignoreCase = true)) {
//                    preferencesSingleton.hour12Format.possibleValues().forEach(Consumer { aBoolean: Boolean -> options.add(aBoolean.toBoolean().toString()) })
//                    stringList = search(args[2], options)
//                    //                    preferencesSingleton.getHour12Format().possibleValues().forEach(s -> {
////                        if(String.valueOf(s.booleanValue()).startsWith(args[2])) {
////                            stringList.add(String.valueOf(s.booleanValue()));
////                        }
////                    });
//                }
//            }
//            return stringList
//        }
//        return ArrayList()
//    }

    companion object {
        const val COMMAND_NAME = "pref"
    }


}