package com.github.fernthedev.fcommands.bungeeclass.files;

import com.github.fernthedev.fcommands.bungeeclass.FileManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private ConfigValues configValues;
    private File configFile;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        configFile = FileManager.getInstance().getConfigfile();

        configValues = new ConfigValues();

        if(!configFile.exists()) {
            saveSettings();
        }

        load();
    }

    public void load() {
        if(configValues == null) {
            configValues = new ConfigValues();
            saveSettings();
        }

        if (configFile.exists()) {
            try {
                try(JsonReader reader = new JsonReader(new FileReader(configFile))) {

                    configValues = gson.fromJson(reader,ConfigValues.class);
                    saveSettings();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            saveSettings();
            load();
        }
    }

    public void saveSettings() {
        if(configValues == null) {
            configValues = new ConfigValues();
        }

        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(gson.toJson(configValues));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ConfigValues getConfigValues() {
        return configValues;
    }
}
