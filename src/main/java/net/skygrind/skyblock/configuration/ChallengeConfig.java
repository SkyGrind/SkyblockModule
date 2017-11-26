package net.skygrind.skyblock.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.challenges.Challenge;
import net.skygrind.skyblock.challenges.ChallengeLevel;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Owned by SethyCorp, and KueMedia respectively.
 **/
public class ChallengeConfig {
    private final transient Gson gson;
    private final transient String fileName;
    private List<Challenge> challenges;
    private List<ChallengeLevel> challengeLevels;

    public ChallengeConfig() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.fileName = ((Plugin) API.getPlugin()).getDataFolder() + File.separator + "skyblock" + File.separator + "challenges.json";
        this.challenges = new ArrayList<>();
        this.challengeLevels = new ArrayList<>();
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
                ChallengeConfig serverConfig = this.gson.fromJson(element, ChallengeConfig.class);
                if(serverConfig == null) {
                    save();
                    return;
                }
                this.challengeLevels = serverConfig.challengeLevels;
                this.challenges = serverConfig.challenges;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public List<ChallengeLevel> getChallengeLevels() {
        return challengeLevels;
    }
}
