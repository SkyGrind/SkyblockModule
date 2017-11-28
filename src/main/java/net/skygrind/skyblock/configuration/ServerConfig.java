package net.skygrind.skyblock.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Owned by SethyCorp, and KueMedia respectively.
 **/
public class ServerConfig {
    private final transient String fileName;
    private final transient Gson gson;
    private GooseLocation spawn;

    public ServerConfig() {
        this.spawn = GooseLocation.fromLocation(new Location(Bukkit.getWorld("world"), 0, 0, 0));
        this.fileName = SkyBlock.getPlugin().getModuleDir() + File.separator + "config.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Location getSpawnLocation() {
        return spawn.toLocation();
    }

    public void setSpawnLocation(final GooseLocation location) {
        this.spawn = location;
    }

    public void save() {
        String json = this.gson.toJson(this);
        System.out.println(json);
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JsonParser parser = new JsonParser();

            try (FileReader fileReader = new FileReader(this.fileName)) {
                JsonElement element = parser.parse(fileReader);
                ServerConfig serverConfig = this.gson.fromJson(element, ServerConfig.class);
                if (serverConfig == null) {
                    save();
                    return;
                }
                this.spawn = serverConfig.spawn;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
