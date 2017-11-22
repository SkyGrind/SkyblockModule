package net.skygrind.skyblock;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import net.skygrind.skyblock.command.RegionCommand;
import net.skygrind.skyblock.command.SetSpawnCommand;
import net.skygrind.skyblock.command.SpawnCommand;
import net.skygrind.skyblock.command.island.InviteAccept;
import net.skygrind.skyblock.command.island.InviteDecline;
import net.skygrind.skyblock.command.island.IslandCommand;
import net.skygrind.skyblock.command.mission.LevelCommand;
import net.skygrind.skyblock.command.shop.ShopAddCategoryCommand;
import net.skygrind.skyblock.command.shop.ShopCreateCommand;
import net.skygrind.skyblock.island.IslandGUIHandler;
import net.skygrind.skyblock.island.IslandOreGens;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.island.listeners.GeneralListener;
import net.skygrind.skyblock.island.listeners.IslandListener;
import net.skygrind.skyblock.misc.LocationUtil;
import net.skygrind.skyblock.missions.MissionRegistry;
import net.skygrind.skyblock.missions.easy.AppleCollector;
import net.skygrind.skyblock.missions.easy.CobbleStone;
import net.skygrind.skyblock.missions.easy.ShroomPicker;
import net.skygrind.skyblock.missions.medium.NovicePvper;
import net.skygrind.skyblock.missions.medium.XPGrinder;
import net.skygrind.skyblock.player.PlayerRegistry;
import net.skygrind.skyblock.region.RegionHandler;
import net.skygrind.skyblock.schematic.SchematicLoader;
import net.skygrind.skyblock.shop.ShopHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import tech.rayline.core.plugin.RedemptivePlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Matt on 2017-02-10.
 */
public class SkyBlock extends RedemptivePlugin {

    private File challenges;

    @Getter
    private FileConfiguration challengesConfig;
    
    private static SkyBlock plugin;
    private PlayerRegistry playerRegistry;
    private RegionHandler regionHandler;
    private IslandRegistry islandRegistry;
    private ShopHandler shopHandler;
    private Location spawn;


    public SchematicLoader schematicLoader;

    private WorldEditPlugin worldEditPlugin;

    private World islandWorld;
    

    /**
     * TODO List for tomorrow
     *
     * Finish Region Command (Create /region create [name] /region delete [name] dispose of active sessions.)
     * Test Schem loading
     * Allow Schems to take a region ?
     */

    @Override
    public void onModuleEnable() {
        plugin = this;
        this.regionHandler = new RegionHandler();
        this.playerRegistry = new PlayerRegistry();
        this.islandRegistry = new IslandRegistry();
        this.shopHandler = new ShopHandler();

        getConfig().options().copyDefaults(true);
        saveConfig();

        islandRegistry.init();

        this.spawn = LocationUtil.deserialize(getConfig().getString("spawn"));

        if (!Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (!Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            Bukkit.getPluginManager().disablePlugin(this);
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
    public void onModuleDisable() {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        saveMissionsFile();
        islandRegistry.disable();
        plugin = null;
    }

    public IslandRegistry getIslandRegistry() {
        return islandRegistry;
    }

    public PlayerRegistry getPlayerRegistry() {
        return playerRegistry;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public SchematicLoader getSchematicLoader() {
        return schematicLoader;
    }

    public static SkyBlock getPlugin() {
        return plugin;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        getConfig().set("spawn", LocationUtil.serialize(spawn));
        saveConfig();
        this.spawn = spawn;
    }


    private void registerClasses() {
        registerListener(playerRegistry);
        
        registerListener(new IslandListener());
        registerListener(new IslandGUIHandler());
        registerListener(new GeneralListener());
        registerListener(new IslandOreGens());

        registerCommand(new IslandCommand());
        registerCommand(new RegionCommand());
        registerCommand(new SetSpawnCommand());
        registerCommand(new SpawnCommand());
        registerCommand(new InviteAccept());
        registerCommand(new InviteDecline());
        registerCommand(new LevelCommand());
        registerCommand(new ShopAddCategoryCommand());
        registerCommand(new ShopCreateCommand());
    }

    private void initFiles() {
        File missionsDir = new File(getDataFolder(), "challenges");
        
        if (!(missionsDir.exists())) {
            missionsDir.mkdir();
        }
        
        challenges = new File(missionsDir,"challenges.yml");
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


    public ShopHandler getShopHandler()
    {
        return shopHandler;
    }
}
