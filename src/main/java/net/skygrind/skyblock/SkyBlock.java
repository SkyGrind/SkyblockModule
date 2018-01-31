package net.skygrind.skyblock;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import com.islesmc.modules.api.module.PluginModule;
import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.item.TabItem;
import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.SimpleTabList;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import net.skygrind.skyblock.command.*;
import net.skygrind.skyblock.command.island.*;
import net.skygrind.skyblock.command.mission.LevelCommand;
import net.skygrind.skyblock.configuration.ChallengeConfig;
import net.skygrind.skyblock.configuration.OreGenerationConfig;
import net.skygrind.skyblock.configuration.ServerConfig;
import net.skygrind.skyblock.configuration.ServerType;
import net.skygrind.skyblock.goose.GooseCommandHandler;
import net.skygrind.skyblock.goose.GooseHandler;
import net.skygrind.skyblock.goose.GooseTicker;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandGUIHandler;
import net.skygrind.skyblock.island.IslandOreGens;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.island.listeners.GeneralListener;
import net.skygrind.skyblock.island.listeners.IslandListener;
import net.skygrind.skyblock.island.listeners.isles.CoordinateBookListener;
import net.skygrind.skyblock.island.listeners.isles.IslesRaidListener;
import net.skygrind.skyblock.player.listener.PlayerBoatListener;
import net.skygrind.skyblock.player.listener.PlayerJoinListener;
import net.skygrind.skyblock.region.RegionHandler;
import net.skygrind.skyblock.schematic.SchematicLoader;
import net.skygrind.skyblock.shop.ShopHandler;
import net.skygrind.skyblock.task.IslandLevelTask;
import net.skygrind.skyblock.util.GridUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private WorldEditPlugin worldEditPlugin;
    private OreGenerationConfig oreGenerationConfig;
    private ServerConfig serverConfig;
    private ChallengeConfig challengeConfig;
    private GooseHandler gooseHandler;
    private Economy economy;
    private Tabbed tabbed;
    private World islandWorld;
    private GridUtil gridUtil;

    public static synchronized SkyBlock getPlugin() {
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
        this.shopHandler = new ShopHandler();
        this.oreGenerationConfig = new OreGenerationConfig();
        this.oreGenerationConfig.loadValues();
        this.serverConfig = new ServerConfig();
        this.serverConfig.load();

        this.islandRegistry = new IslandRegistry();

        this.challengeConfig = new ChallengeConfig();
        this.challengeConfig.load();
        this.gooseHandler = new GooseHandler();
        this.tabbed = new Tabbed(API.getPlugin());
        this.gridUtil = new GridUtil(100000);

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null && !Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled()) {
            disable();
        }

        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

//        if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null && !Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
//            disable();
//

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getServerConfig().getServerType() == ServerType.SKY) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv create Skyblock normal -g VoidWorld");
                }

                islandWorld = Bukkit.getWorld("Skyblock");
                setupEconomy();
            }
        }.runTaskLater(API.getPlugin(), 20L);

        schematicLoader = new SchematicLoader();

//        initFiles();
        setupShit();
        //TODO load schems
        //TODO load player data

        new GooseTicker().runTaskTimerAsynchronously(API.getPlugin(), 1L, 1L);
        new IslandLevelTask().runTaskTimerAsynchronously(API.getPlugin(), 20L, TimeUnit.MINUTES.toMillis(10) * 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    SimpleTabList tab = (SimpleTabList) getTabbed().getTabList(player);
                    int i = 0;
                    for (TabItem tabItem : getTabItems(player)) {
                        if (tab == null || tabItem == null)
                            continue;

                        tab.set(i, new TextTabItem(ChatColor.translateAlternateColorCodes('&', tabItem.getText())));
                        i++;
                    }
                }
            }
        }.runTaskTimerAsynchronously(API.getPlugin(), 20L, 20L);
    }

    private boolean setupEconomy() {
        if (API.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            System.out.println("no vault");
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = API.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) {
            System.out.println("rsp is null");
            return false;
        }
        economy = economyProvider.getProvider();
        return economy != null;
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
        getServerConfig().save();
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

    private void setupShit() {
        registerEvent(new IslandListener());
        registerEvent(new IslandGUIHandler());
        registerEvent(new GeneralListener());
        registerEvent(new IslandOreGens());
        registerEvent(this.gooseHandler);
        registerEvent(new PlayerJoinListener());

        if (getServerConfig().getServerType().equals(ServerType.ISLES)) {
            registerEvent(new IslesRaidListener());
            registerEvent(new CoordinateBookListener());
            registerEvent(new PlayerBoatListener());
            registerCommand("givecoordinatebook", new GiveCoordinateBookCommand());
        }

        GooseCommandHandler commandHandler = new GooseCommandHandler("island", new IslandBaseCommand());
        commandHandler.setAliases(Lists.newArrayList("is", "islands"));
        commandHandler.addSubCommand("accept", new IslandAcceptCommand());
        commandHandler.addSubCommand("help", new IslandBaseCommand());
        commandHandler.addSubCommand("create", new IslandCreateCommand());
        commandHandler.addSubCommand("decline", new IslandDeclineCommand());
        commandHandler.addSubCommand("delete", new IslandDeleteCommand());
        commandHandler.addSubCommand("home", new IslandHomeCommand());
        commandHandler.addSubCommand("kick", new IslandKickCommand());
        commandHandler.addSubCommand("leave", new IslandLeaveCommand());
        commandHandler.addSubCommand("level", new IslandLevelCommand());
        commandHandler.addSubCommand("invite", new IslandInviteCommand());
//        commandHandler.addSubCommand(new IslandLockCommand());
        commandHandler.addSubCommand("top", new IslandTopCommand());
        commandHandler.addSubCommand("lock", new IslandLockCommand());
        commandHandler.addSubCommand("warp", new IslandWarpCommand());
        commandHandler.addSubCommand("sethome", new IslandSetHomeCommand());

        registerCommand("island", commandHandler);

        registerCommand("region", new RegionCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("level", new LevelCommand());
        registerCommand("balance", new BalanceCommand());
        registerCommand("pay", new PayCommand());
        registerCommand("setbalance", new AddBalanceCommand());
        registerCommand("help", new HelpCommand());
        registerCommand("setislandsize", new SetIslandSizeCommand());
        registerCommand("setmaxmembers", new SetIslandMaxMembersCommand());
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


    public Economy getEconomy() {
        return this.economy;
    }

    public String format(double number) {
        String[] suffix = new String[]{"", "K", "M", "B", "T", "Q"};
        int MAX_LENGTH = 4;
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    public ShopHandler getShopHandler() {
        return shopHandler;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public GooseHandler getGooseHandler() {
        return gooseHandler;
    }

    public List<TabItem> getTabItems(final Player player) {
        String primaryColor = getServerConfig().getPrimaryColor();
        List<TabItem> items = new ArrayList<>();

        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        if (user == null)
            return items;

        Profile permissions = user.getProfile("permissions");

        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lIsland"));
        Island island = getIslandRegistry().getIslandForPlayer(player);
        if (island == null) {
            items.add(new TextTabItem("&fNone"));
            items.add(new TextTabItem(" "));
            items.add(new TextTabItem(" "));
            items.add(new TextTabItem(" "));
            items.add(new TextTabItem(" "));
        } else {
            items.add(new TextTabItem(island.getName()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Level&7: &f" + island.getIslandLevel()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Balance&7: &f" + island.getBankBalance()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Members&7: &f" + island.getMembers().size() + "/" + island.getMaxPlayers()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Type&7: &f" + island.getType().getRaw()));
        }
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lCommunity"));
        items.add(new TextTabItem("&fSkyParadise-mc.com"));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));

        items.add(new TextTabItem("&b&lSky&f&lParadise"));
        items.add(new TextTabItem("&fOnline: " + Bukkit.getOnlinePlayers().size()));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "Top Islands"));
        // 4/21
        int i = 0;
        int max = 10;
        if (SkyBlock.getPlugin().getIslandRegistry().playerIslands.size() < 10) {
            max = SkyBlock.getPlugin().getIslandRegistry().playerIslands.size();
        }
        for (Map.Entry<Island, Integer> entry : IslandTopCommand.getTopIslands().subList(0, max)) {
            i++;
            items.add(new TextTabItem("&f" + entry.getKey().getName() + " (" + entry.getKey().getIslandLevel() + ")"));
        }
        while (i <= 13) {
            i++;
            items.add(new TextTabItem(" "));
        }

        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lStore"));
        items.add(new TextTabItem("&fshop.skyparadisemc.com"));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));

        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lPlayer Info"));
        if (permissions == null) {
            items.add(new TextTabItem("&7Group: &f" + "Member"));
        } else {
            items.add(new TextTabItem("&7Group: &f" + permissions.getString("group")));
        }
        items.add(new TextTabItem("&7Balance: &f$" + format(getEconomy().getBalance(player))));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        return items;
    }

    public Tabbed getTabbed() {
        return tabbed;
    }

    public GridUtil getGridUtil() {
        return gridUtil;
    }
}
