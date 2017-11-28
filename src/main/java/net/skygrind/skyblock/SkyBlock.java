package net.skygrind.skyblock;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.module.PluginModule;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.skygrind.skyblock.command.RegionCommand;
import net.skygrind.skyblock.command.SetSpawnCommand;
import net.skygrind.skyblock.command.SpawnCommand;
import net.skygrind.skyblock.command.island.*;
import net.skygrind.skyblock.command.mission.LevelCommand;
import net.skygrind.skyblock.configuration.ChallengeConfig;
import net.skygrind.skyblock.configuration.OreGenerationConfig;
import net.skygrind.skyblock.configuration.ServerConfig;
import net.skygrind.skyblock.goose.GooseCommandHandler;
import net.skygrind.skyblock.island.IslandGUIHandler;
import net.skygrind.skyblock.island.IslandOreGens;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.island.listeners.GeneralListener;
import net.skygrind.skyblock.island.listeners.IslandListener;
import net.skygrind.skyblock.region.RegionHandler;
import net.skygrind.skyblock.schematic.SchematicLoader;
import net.skygrind.skyblock.shop.ShopHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Created by Matt on 2017-02-10.
 */
public class SkyBlock extends PluginModule {

    private static SkyBlock plugin;
    public SchematicLoader schematicLoader;
    private File challenges;
    private FileConfiguration challengesConfig;
    private RegionHandler regionHandler;
    private IslandRegistry islandRegistry;
    private ShopHandler shopHandler;
    private Location spawn;
    private WorldEditPlugin worldEditPlugin;
    private OreGenerationConfig oreGenerationConfig;
    private ServerConfig serverConfig;
    private ChallengeConfig challengeConfig;

    private World islandWorld;

    public static SkyBlock getPlugin() {
        return plugin;
    }

    /**
     * TODO List for tomorrow
     * <p>
     * Finish Region Command (IslandCreateCommand /region create [name] /region delete [name] dispose of active sessions.)
     * Test Schem loading
     * Allow Schems to take a region ?
     */

    @Override
    public void onEnable() {
        getModuleDir().toFile().mkdir();
        plugin = this;
        this.regionHandler = new RegionHandler();
        this.islandRegistry = new IslandRegistry();
        this.shopHandler = new ShopHandler();
        this.oreGenerationConfig = new OreGenerationConfig();
        this.oreGenerationConfig.loadValues();
        this.serverConfig = new ServerConfig();
        this.serverConfig.load();
        this.challengeConfig = new ChallengeConfig();
        this.challengeConfig.load();

        islandRegistry.init();

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null && !Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled()) {
            API.getModuleManager().disableModule(this);
        }

        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null && !Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            API.getModuleManager().disableModule(this);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv create Skyblock normal -g VoidWorld");

        islandWorld = Bukkit.getWorld("Skyblock");

        schematicLoader = new SchematicLoader();

//        initFiles();
        setupShit();
        //TODO load schems
        //TODO load player data
    }

    public RegionHandler getRegionHandler() {
        return regionHandler;
    }

    public World getIslandWorld() {
        return islandWorld;
    }

    @Override
    public void onDisable() {
//        saveMissionsFile();
        islandRegistry.disable();
        plugin = null;
    }

    public ChallengeConfig getChallengeConfig() {
        return challengeConfig;
    }

    public OreGenerationConfig getOreGenerationConfig() {
        return oreGenerationConfig;
    }

    public IslandRegistry getIslandRegistry() {
        return islandRegistry;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public SchematicLoader getSchematicLoader() {
        return schematicLoader;
    }

    public Location getSpawn() {
        return spawn;
    }

    private void setupShit() {
        registerEvent(new IslandListener());
        registerEvent(new IslandGUIHandler());
        registerEvent(new GeneralListener());
        registerEvent(new IslandOreGens());

        GooseCommandHandler commandHandler = new GooseCommandHandler("island", new IslandBaseCommand());
        commandHandler.addSubCommand("accept", new IslandAcceptCommand());
        commandHandler.addSubCommand("help", new IslandBaseCommand());
        commandHandler.addSubCommand("create", new IslandCreateCommand());
        commandHandler.addSubCommand("decline", new IslandDeclineCommand());
        commandHandler.addSubCommand("delete", new IslandDeleteCommand());
        commandHandler.addSubCommand("home", new IslandHomeCommand());
        commandHandler.addSubCommand("kick", new IslandKickCommand());
        commandHandler.addSubCommand("leave", new IslandLeaveCommand());
        commandHandler.addSubCommand("level", new IslandLevelCommand());
//        commandHandler.addSubCommand(new IslandLockCommand());
        commandHandler.addSubCommand("top", new IslandTopCommand());

        registerCommand("island", commandHandler);

        registerCommand("region", new RegionCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("level", new LevelCommand());
    }

//    private void initFiles() {
//        File missionsDir = new File(getModuleDir().toString(), "challenges");
//
//        if (!(missionsDir.exists())) {
//            missionsDir.mkdir();
//        }
//
//        challenges = new File(missionsDir, "challenges.yml");
//        challengesConfig = YamlConfiguration.loadConfiguration(challenges);
//
//        challengesConfig.options().copyDefaults(true);
//
//        if (!(challenges.exists())) {
//            try {
//                System.out.println("Missions file not found. Creating....");
//                challenges.createNewFile();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        challengesConfig.options().copyDefaults(true);
//    }
//    public void saveMissionsFile() {
//        try {
//            if (challenges == null) {
//                System.out.println("No data inside of missions file!");
//                return;
//            }
//            challengesConfig.save(challenges);
//        } catch (IOException ex) {
//            System.out.println("There was a problem saving " + challenges.toString());
//        }
//    }
//
//    public void reloadMissions() {
//        saveMissionsFile();
//        YamlConfiguration.loadConfiguration(challenges);
//    }


    public ShopHandler getShopHandler() {
        return shopHandler;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }
}
