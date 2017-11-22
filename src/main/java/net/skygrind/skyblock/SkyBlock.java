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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import xyz.sethy.commands.CommandHandler;

import java.io.File;
import java.io.IOException;

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

        if (!Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled()) {
            API.getModuleManager().disableModule(this);
        }

        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (!Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            API.getModuleManager().disableModule(this);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv create Skyblock normal -g VoidWorld");

        islandWorld = Bukkit.getWorld("Skyblock");

        schematicLoader = new SchematicLoader();

        initFiles();
        registerClasses();
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
        saveMissionsFile();
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

    private void registerClasses() {
        registerEvent(new IslandListener());
        registerEvent(new IslandGUIHandler());
        registerEvent(new GeneralListener());
        registerEvent(new IslandOreGens());

        CommandHandler commandHandler = new CommandHandler("island");
        commandHandler.addSubCommand(new IslandAcceptCommand());
        commandHandler.addSubCommand(new IslandBaseCommand());
        commandHandler.addSubCommand(new IslandDeclineCommand());
        commandHandler.addSubCommand(new IslandDeleteCommand());
        commandHandler.addSubCommand(new IslandHomeCommand());
        commandHandler.addSubCommand(new IslandKickCommand());
        commandHandler.addSubCommand(new IslandLeaveCommand());
        commandHandler.addSubCommand(new IslandLevelCommand());
//        commandHandler.addSubCommand(new IslandLockCommand());
        commandHandler.addSubCommand(new IslandTopCommand());

        registerCommand("island", commandHandler);

        registerCommand("region", new RegionCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("level", new LevelCommand());
    }

    private void initFiles() {
        File missionsDir = new File(((Plugin) API.getPlugin()).getDataFolder(), "challenges");

        if (!(missionsDir.exists())) {
            missionsDir.mkdir();
        }

        challenges = new File(missionsDir, "challenges.yml");
        challengesConfig = YamlConfiguration.loadConfiguration(challenges);

        challengesConfig.options().copyDefaults(true);

        if (!(challenges.exists())) {
            try {
                System.out.println("Missions file not found. Creating....");
                challenges.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        challengesConfig.options().copyDefaults(true);
    }


    public void saveMissionsFile() {
        try {
            if (challenges == null) {
                System.out.println("No data inside of missions file!");
                return;
            }
            challengesConfig.save(challenges);
        } catch (IOException ex) {
            System.out.println("There was a problem saving " + challenges.toString());
        }
    }

    public void reloadMissions() {
        saveMissionsFile();
        YamlConfiguration.loadConfiguration(challenges);
    }


    public ShopHandler getShopHandler() {
        return shopHandler;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }
}
