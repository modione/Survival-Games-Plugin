package me.modione.sgplugin.utils;

import java.io.IOException;
import me.modione.sgplugin.SGPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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
