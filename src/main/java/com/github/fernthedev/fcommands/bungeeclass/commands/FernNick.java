package com.github.fernthedev.fcommands.bungeeclass.commands;

import com.github.fernthedev.fcommands.bungeeclass.DatabaseHandler;
import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class FernNick extends FCommand implements Listener {
    public FernNick() {
        super("fnick", "fernc.nick", "gnick","nick","fnick");
        setupTable();
    }

    private void setupTable() {
        try {
            Statement statement = DatabaseHandler.statement();
            if(statement != null) {
                //statement.executeUpdate("CREATE TABLE IF NOT EXISTS fern_nicks (PlayerUUID varchar(200), nick varchar(40))");
                String sql = "CREATE TABLE IF NOT EXISTS fern_nicks(PLAYERUUID varchar(200), NICK varchar(40));";

                PreparedStatement stmt = DatabaseHandler.getConnection().prepareStatement(sql);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Connection connection = DatabaseHandler.getConnection();

        ByteArrayOutputStream outputStream;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        if(args.length == 1) {
            if(args[0].contains("&") && !sender.hasPermission("fernc.nick.color")) {
             sendMessage(sender,"&cYou do not have permissions to use color nicknames.");
             return;
            }
            try {
                if(connection != null) {

                    String formattedUUID = player.getUniqueId().toString().replaceAll("-","");

                    String sql = "DELETE FROM fern_nicks WHERE PLAYERUUID='" + formattedUUID + "';";
                    Universal.getMethods().getLogger().info(sql);

                    PreparedStatement stmt = connection.prepareStatement(sql);

                    stmt.executeUpdate();



                        //sql = "SELECT CASE WHEN PLAYERUUID="+player.getUniqueId().toString()+ "THEN NICK "+args[0];
                        sql = "UPDATE fern_nicks SET PLAYERUUID='" + player.getUniqueId().toString().replaceAll("-","") + "NICK='"+args[0]+"' WHERE PLAYERUUID='"+player.getUniqueId().toString().replaceAll("-","") + "';";
                        stmt = connection.prepareStatement(sql);

                        connection.prepareStatement
                                ("INSERT INTO fern_nicks (PLAYERUUID, NICK) VALUES ('" +
                                        ProxyServer.getInstance().getPlayer(sender.getName()).getUniqueId().toString().replaceAll("-","") + "','" + args[0] + "');").executeUpdate();


                    out.writeUTF("Forward"); //TYPE
                    out.writeUTF("ALL"); //SERVER
                    out.writeUTF("ReloadNickSQL"); //SUBCHANNEL

                    out.writeUTF(player.getName()); //NAME
                    out.writeUTF(player.getUniqueId().toString().replaceAll("-","")); //UUID

                    outputStream = stream;

                    Universal.getMethods().getLogger().info(outputStream.toString() + " is the info sent.");

                ProxyServer.getInstance().getServers().values().forEach(serverInfo -> serverInfo.sendData("BungeeCord",outputStream.toByteArray()));




                //sendMessage(sender,"&aSuccessfully set your nick to " + args[0]);
                //sendMessage(sender,"&aPlease relog for this to take effect.");
                }else {
                    sendMessage(sender, "&cUnable to set your nick, please contact a fern");
                    throw new SQLException();
                }
            } catch (SQLException | IOException e) {
                sendMessage(sender, "&cUnable to set your nick, please contact a fern");
                e.printStackTrace();
            }

        }
        else if(args.length >= 2) {
            if (sender.hasPermission("fernc.nick.other")) {
                if (ProxyServer.getInstance().getPlayer(args[0]) != null) {
                    ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(args[0]);
                    try {
                        if (connection != null) {

                            String formattedUUID = player1.getUniqueId().toString().replaceAll("-","");

                            String sql = "DELETE FROM fern_nicks WHERE PLAYERUUID='" + formattedUUID + "';";
                            FernCommands.getInstance().getLogger().info(sql + " is a thing");

                            int teest = connection.prepareStatement("DELETE FROM fern_nicks WHERE PLAYERUUID='" + formattedUUID + "';").executeUpdate();



                                //sql = "SELECT CASE WHEN PLAYERUUID="+player1.getUniqueId().toString()+ "THEN NICK "+args[0];
                                //sql = "UPDATE fern_nicks SET PLAYERUUID='" + formattedUUID + "NICK='"+args[0]+"' WHERE PLAYERUUID='"+player1.getUniqueId().toString().replaceAll("-","") + "';";

                                connection.prepareStatement
                                        ("INSERT INTO fern_nicks (PLAYERUUID, NICK) VALUES ('" +
                                                ProxyServer.getInstance().getPlayer(sender.getName()).getUniqueId().toString().replaceAll("-","") + "','" + args[0] + "');").executeUpdate();


                            out.writeUTF("Forward"); //TYPE
                            out.writeUTF("ALL"); //SERVER
                            out.writeUTF("ReloadNickSQL"); //SUBCHANNEL

                            out.writeUTF(player1.getName()); //NAME
                            out.writeUTF(player1.getUniqueId().toString().replaceAll("-","")); //UUID

                            outputStream = stream;

                            Universal.getMethods().getLogger().info("A result");
                            FernCommands.getInstance().getLogger().info(teest + " is the result");
                            Universal.getMethods().getLogger().info(outputStream.toString() + " is the info sent. again");

                            ProxyServer.getInstance().getServers().values().forEach(serverInfo -> serverInfo.sendData("BungeeCord",outputStream.toByteArray()));


                            
                        } else {
                            sendMessage(sender, "&cUnable to set your nick, please contact a fern");
                            throw new SQLException();
                        }
                    } catch (SQLException | IOException e) {
                        sendMessage(sender, "&cUnable to set your nick, please contact a fern");
                        e.printStackTrace();
                    }

                } else {
                    sendMessage(sender, "&cUnable to find player " + args[0]);
                }
            }else{
                sendMessage(sender,"&cYou do not have permission to nick others do that.");
            }
        }
        else{
            sendMessage(sender,"&cNo nick?");
        }
    }
}
