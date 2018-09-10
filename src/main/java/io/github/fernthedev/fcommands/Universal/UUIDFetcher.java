package io.github.fernthedev.fcommands.Universal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.fernthedev.fcommands.bungeeclass.FernCommands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UUIDFetcher {
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%name%";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%uuid%/names";

    private UUIDFetcher() {
    }

    public static String getUUID(String name) {
        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // read JSON file data as String
        try {
            String fileData = readUrl(UUID_URL.replace("%name%",name));

            PlayerUUID uuidResponse = gson.fromJson(fileData,PlayerUUID.class);

            FernCommands.getInstance().getLogger().info("The uuid for " + name + " is " + uuidResponse.getId());

            return uuidResponse.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getName(String uuid) {
        // Get Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        uuid = uuid.replaceAll("-","");
        // read JSON file data as String
        try {
            String fileData = readUrl(NAME_URL.replace("%uuid%",uuid));
            FernCommands.getInstance().getLogger().info("The url of uuid is " + NAME_URL.replace("%uuid%",uuid));

            PlayerName[] uuidResponse = gson.fromJson(fileData,PlayerName[].class);

            if(uuidResponse != null) {
                //FernCommands.getInstance().getLogger().info(gson.toJson(uuidResponse));
                FernCommands.getInstance().getLogger().info("The max length of response is " + uuidResponse.length);
                for (int i =0; i < uuidResponse.length; i++) {
                    PlayerName playerUUID = uuidResponse[i];
                    FernCommands.getInstance().getLogger().info("A name from uuid " + uuid + " is " + playerUUID.name + " at length " + i);
                }

                if (uuidResponse.length > 0) {

                    PlayerName currentName = uuidResponse[uuidResponse.length - 1];

                    FernCommands.getInstance().getLogger().info("The current name is " + currentName.name);

                    return currentName.name;
                } else {
                    return null;
                }
            }else{
                FernCommands.getInstance().getLogger().info("The response was empty");
                FernCommands.getInstance().getLogger().info("The response received is " + fileData);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This can be used to get name history
     * @param uuid The uuid of the player
     * @return The name history.
     */
    public static List<PlayerHistory> getNameHistory(String uuid) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        uuid = uuid.replaceAll("-","");
        // read JSON file data as String
        try {
            String fileData = readUrl(NAME_URL.replace("%uuid%",uuid));
            FernCommands.getInstance().getLogger().info("The url of uuid is " + NAME_URL.replace("%uuid%",uuid));

            PlayerName[] uuidResponse = gson.fromJson(fileData,PlayerName[].class);

            if(uuidResponse != null) {
                //FernCommands.getInstance().getLogger().info(gson.toJson(uuidResponse));
                FernCommands.getInstance().getLogger().info("The max length of response is " + uuidResponse.length);
                for (int i =0; i < uuidResponse.length; i++) {
                    PlayerName playerUUID = uuidResponse[i];
                    FernCommands.getInstance().getLogger().info("A name from uuid " + uuid + " is " + playerUUID.name + " at length " + i);
                }

                if (uuidResponse.length > 0) {

                    List<PlayerHistory> names = new ArrayList<>();
                    for(PlayerName currentResponse : uuidResponse) {
                        names.add(new PlayerHistory(currentResponse.name,new Date(currentResponse.getChangedToAt()),currentResponse.changedToAt));
                    }

                    return names;
                } else {
                    return null;
                }
            }else{
                FernCommands.getInstance().getLogger().info("The response was empty");
                FernCommands.getInstance().getLogger().info("The response received is " + fileData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public static class PlayerHistory {
        private String name;
        private Date date;
        private long timeDate;

        PlayerHistory(String name,Date time,long timeDate) {
            this.name = name;
            this.date = time;
            this.timeDate = timeDate;
        }

        public long getTimeDateInt() {
            return timeDate;
        }

        public String getName() {
            return name;
        }

        public Date getTime() {
            return date;
        }
    }
@SuppressWarnings("unused")
    class PlayerName {
        private String name;
        private long changedToAt;

        public String getName() {
            return name;
        }

        public long getChangedToAt() {
            return changedToAt;
        }
    }

@SuppressWarnings("unused")
    class PlayerUUID {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

    }
}