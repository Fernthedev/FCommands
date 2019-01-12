# FCommands
Fern Commands :P
A plugin I made because I get bored

[o] = can be used on other players

[c] = Command

[b] = Bungee Permissions required (Used in spigot/universal features)

[f] = Configurable in config

# Bungee Features:
- NameHistory (/nh,/namehistory, /fnh fernc.namehistory) [o] [c] [f]
- PingShower (/fernb fern.test) [o] [c] [f]
- Last Seen (/seen fernc.seen) [o] [c] [f]
- Alt ban (Advancedban required) [f]
- Alt List (/accounts fernc.accounts.see) [o] [c] [f]
- Show current ip and alts from current ip (/myip fernc.myip.see) [c] [f]
- Shows a special MOTD for banned players [f]
- Shows a special MOTD when a server running a list of ports in config is down (Currently not configurable, to change soon) [f]


# Spigot Features:
- NameColor (/namecolor fernc.namecolor) [f]
- Attempts to fix NTE/McMMO incompability in paper
- NoAI for mobs spawned by custom methods, such as server or plugins or spawners. [f]
- NoCheatPlus bungee compability [f]
- BedFire, to make players go in fire when sleeping for a prank [f]
- LavaBurn, makes players burn when picking up lava or cactus. [f]
- GodPearl, disable damage caused by enderpearls [f]
- IgDoorFarm, causes the old iron golem farm made with doors disable [f]
- RideBow, a bow that teleports you to an arrow where it lands [f]
- Skylands, requires SB-Skylands (which does work if compiled from source). Using enderpearls at max height looking up will teleport you to the skylands, falling to the skylands teleport you to the overworld falling.

# Universal Features:
- Global nick (/gnick or /nick fernc.nick | requires MySQL) [c] [b]
- Get Placeholder from spigot servers (Requires PlaceHolderAPI installed on spigot) [o] [c]

# Universal API (API for both spigot & bungee):
- Get Placeholder from spigot server, but within java code. 
- UUID Fetcher (is being replaced by [FernAPI](https://github.com/Fernthedev/FernAPI))



## Api Usage:
Get Placeholder from spigot server. Example: (Bungee only)
```java
AskPlaceHolder askPlaceHolder = new AskPlaceHolder(Player,PlaceHolder);

askPlaceHolder.setRunnable(new MessageRunnable() {
    @Override
    public void run() {
        super.run();
        sender.sendMessage("The player's placeholder value of " + args[1] + " is " + askPlaceHolder.getPlaceHolderResult());
    }
});
```

Get UUID from server. (Used from [FernAPI](https://github.com/Fernthedev/FernAPI)) Ex:
```java
String name = UUIDFetcher.getName(uuid);
String UUID = UUIDFetcher.getUUID(name);
List<UUIDFetcher.PlayerHistory> names = UUIDFetcher.getNameHistory(uuidPlayer);
```
