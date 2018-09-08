package io.github.fernthedev.fcommands.bungeeclass.commands.ip;

import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import io.github.fernthedev.fcommands.bungeeclass.FileManager;
import io.github.fernthedev.fcommands.bungeeclass.hooks;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AltsBan implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        if (hooks.getInstance().hasIsAdvancedBan()) {
            ProxiedPlayer player = e.getPlayer();
            Configuration ipconfig = new FileManager().getIpconfig();

            String uuidPlayer = player.getUniqueId().toString();

            String ip = player.getAddress().getHostString();
            ip = ip.replaceAll("\\.", " ");


            //String uuid = p.getUniqueId().toString();

            boolean ipfileloaded;

            try {
                new FileManager().loadFiles("ip", true);
                ipfileloaded = true;
            } catch (IOException ee) {
                FernCommands.getInstance().getLogger().warning("Unable to load ips");
                ipfileloaded = false;
            }

            if (ipfileloaded) {
                List<String> players = ipconfig.getStringList(ip);

                List<String> ips = new ArrayList<>();
                ips.add(ip);

                for (String ipe : ipconfig.getKeys()) {
                    if (!ipe.equals(ip)) {
                        List<String> playips = ipconfig.getStringList(ipe);
                        if (!playips.isEmpty() && playips.contains(uuidPlayer)) {
                            for (String pluuid : playips) {
                                if (!players.contains(pluuid)) {
                                    players.add(pluuid);
                                }
                            }
                        }
                    }
                }


                for (String uuid : players) {
                    String playername = UUIDFetcher.getName(UUIDFetcher.getUUID(uuid));
                    //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);

                    if (PunishmentManager.get().isBanned(uuid) || PunishmentManager.get().isMuted(uuid)) {
                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.MUTE, true).isEmpty()) {
                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.MUTE, true)) {
                                new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                            }
                        }

                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_MUTE, true).isEmpty()) {
                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_MUTE, true)) {
                                new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                            }
                        }

                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.BAN, true).isEmpty()) {

                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.BAN, true)) {
                                new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                            }
                        }

                        if (!PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_BAN, true).isEmpty()) {
                            for (Punishment punishment : PunishmentManager.get().getPunishments(uuid, PunishmentType.TEMP_BAN, true)) {
                                new Punishment(playername, uuid, punishment.getReason() + "&6From an alt which is " + ProxyServer.getInstance().getPlayer(punishment.getUuid()) + punishment.getName(), punishment.getOperator(), punishment.getType(), punishment.getStart(), punishment.getEnd(), punishment.getCalculation(), -1);
                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onPunish(PunishmentEvent e) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(e.getPunishment().getUuid());

        if(player == null) {
            FernCommands.getInstance().getLogger().info("There was an issue finding a punished player with the uuid of " + e.getPunishment().getUuid());
            return;
        }

        Configuration ipconfig = new FileManager().getIpconfig();

        String uuidPlayer = player.getUniqueId().toString();

        String ip = player.getAddress().getHostString();
        ip = ip.replaceAll("\\.", " ");


        //String uuid = p.getUniqueId().toString();

        boolean ipfileloaded;

        try {
            new FileManager().loadFiles("ip",true);
            ipfileloaded = true;
        } catch (IOException ee) {
            FernCommands.getInstance().getLogger().warning("Unable to load ips");
            ipfileloaded = false;
        }

        if(ipfileloaded) {
            List<String> players = ipconfig.getStringList(ip);

            List<String> ips = new ArrayList<>();
            ips.add(ip);

            for(String ipe : ipconfig.getKeys()) {
                if (!ipe.equals(ip)) {
                    List<String> playips = ipconfig.getStringList(ipe);
                    if (!playips.isEmpty() && playips.contains(uuidPlayer)) {
                        for (String pluuid : playips) {
                            if (!players.contains(pluuid)) {
                                players.add(pluuid);
                            }
                        }
                    }
                }
            }



            for(String uuid : players) {
                //ProxiedPlayer playerListUUID = ProxyServer.getInstance().getPlayer(uuid);
                String playername = UUIDFetcher.getName(UUIDFetcher.getUUID(uuid));
                new Punishment(playername,uuid,e.getPunishment().getReason() + " &6From an alt which is " + ProxyServer.getInstance().getPlayer(e.getPunishment().getUuid()),e.getPunishment().getOperator(),e.getPunishment().getType(),e.getPunishment().getStart(),e.getPunishment().getEnd(),e.getPunishment().getCalculation(),-1);
            }
            }

    }

    public String getNameByUUI(String uuid) {
        URL url = null;
        InputStreamReader in = null;
        try {
            url = new URL("https://api.mojang.com/user/profiles/UUID/names".replace("UUID", uuid.replace("-", "")));
            try {
                in = new InputStreamReader(url.openStream());
                return FernCommands.getGson().fromJson(in, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(in != null)
                    in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

}
