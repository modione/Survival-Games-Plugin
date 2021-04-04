package me.modione.sgplugin.utils;

import me.modione.sgplugin.SGPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class FileConfig extends YamlConfiguration {

    private final String path;

    public FileConfig(String filename) {
        this.path = SGPlugin.INSTANCE.path + filename;
        try {
            load(this.path);
        } catch (InvalidConfigurationException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveconfig() {
        try {
            save(this.path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
