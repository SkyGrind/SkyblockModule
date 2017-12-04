package net.skygrind.skyblock.island;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.LocationUtil;
import net.skygrind.skyblock.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Matt on 2017-02-11.
 */
public class IslandRegistry {

    private final int islandDistance = 1000;
    private final int baseIslandSize = 80;
    public List<Island> playerIslands = new ArrayList<>();
    private File islandDir = new File(SkyBlock.getPlugin().getModuleDir().toString(), "islands");
    private LinkedHashMap<UUID, Island> islandInvites = new LinkedHashMap<>();
    private Location lastIsland = null;

    public static Location alignToDistance(Location loc, int distance) {
        if (loc == null) {
            return null;
        }
        int x = (int) (Math.round(loc.getX() / distance) * distance);
        int z = (int) (Math.round(loc.getZ() / distance) * distance);
        loc.setX(x);
        loc.setY(100);
        loc.setZ(z);
        return loc;
    }

    public void init() {
        loadIslands();
    }

    public void registerIsland(UUID owner, Island island) {
        this.playerIslands.add(island);
    }

    public void disable() {
        for (Island island : playerIslands) {


//            File islandFile = getFileForIsland(island);
//            YamlConfiguration config = YamlConfiguration.loadConfiguration(islandFile);
//
//            if (island == null) {
//                return;
//            }
//
//            config.set("owner", island.getOwner());
//            config.set("min", LocationUtil.serialize(island.getContainer().getMin()));
//            config.set("max", LocationUtil.serialize(island.getContainer().getMax()));
//            List<String> mems = config.getStringList("members");
//            for (UUID uuid : island.getMembers()) {
//                if (!mems.contains(uuid.toString())) {
//                    continue;
//                }
//                mems.add(uuid.toString());
//            }
//            config.set("members", mems);
//            config.set("spawn", island.getSpawn());
//            config.set("maxPlayers", island.getMaxPlayers());
//            config.set("balance", island.getBankBalance());
            try {
                island.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasIsland(Player player) {
        for (Island island : playerIslands) {
            if (island.getOwner().equals(player.getUniqueId()) || island.getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public List<Island> getPlayerIslands() {
        return playerIslands;
    }

    private void loadIslands() {
        if (!islandDir.exists()) {
            islandDir.mkdir();
        }

        File[] files = islandDir.listFiles();

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            JsonParser parser = new JsonParser();


            try (FileReader reader = new FileReader(file)) {
                JsonElement element = parser.parse(reader);
                Island island = new GsonBuilder().setPrettyPrinting().create().fromJson(element, Island.class);
                this.playerIslands.add(island);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        for (File islandFile : islandDir.listFiles()) {
//            if (islandFile.getName().endsWith(".yml")) {
//                //ISLAND!!
//
//                YamlConfiguration islandConfig = YamlConfiguration.loadConfiguration(islandFile);
//
//                UUID ownerID = UUID.fromString(islandConfig.getString("ownerID"));
//                Location spawn = LocationUtil.deserialize(islandConfig.getString("spawn"));
//
//                Location min = LocationUtil.deserialize(islandConfig.getString("min"));
//                Location max = LocationUtil.deserialize(islandConfig.getString("max"));
//
//                IslandType type = IslandType.valueOf(islandConfig.getString("type"));
//
//                int bankBalance = islandConfig.getInt("balance");
//                int islandLevel = islandConfig.getInt("islandLevel");
//                int maxPlayers = islandConfig.getInt("maxPlayers");
//
//                List<UUID> members = new ArrayList<>();
//
//                for (String uuid : islandConfig.getStringList("members")) {
//                    UUID id = UUID.fromString(uuid);
//                    members.add(id);
//                }
//
//                Island island = new Island(ownerID, spawn, type);
//                island.setContainer(SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max));
//                island.setMembers(members);
//                island.setMaxPlayers(maxPlayers);
//                island.setBankBalance(bankBalance);
//                island.setIslandLevel(islandLevel);
//
//                registerIsland(ownerID, island);
//            }
//        }
        System.out.println("Loaded: " + playerIslands.size());
        if (playerIslands.isEmpty()) {

        } else {
            this.lastIsland = playerIslands.get(playerIslands.size() - 1).getSpawn();
        }
    }

    public void createIsland(Player player, IslandType type) throws MaxChangedBlocksException {

        if (hasIsland(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You already have an island!");
            return;
        }

        //Location center = findEmptySpace();
        Location center = nextIslandLocation(lastIsland == null ? new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0) : lastIsland);

        Island island = new Island(player.getUniqueId(), center, type);
        island.setSize(baseIslandSize);

        double minX = center.getBlockX() - island.getType().getSize() / 2;
        double minY = 0;
        double minZ = center.getBlockZ() - island.getType().getSize() / 2;

        int maxX = center.getBlockX() + island.getType().getSize() / 2;
        int maxY = 256;
        int maxZ = center.getBlockZ() + island.getType().getSize() / 2;


        Location min = new Location(SkyBlock.getPlugin().getIslandWorld(), minX, minY, minZ);
        Location max = new Location(SkyBlock.getPlugin().getIslandWorld(), maxX, maxY, maxZ);

        Region container = SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max);
        island.setContainer(container);
        island.setMembers(new ArrayList<>());
        island.setIslandLevel(0);

        island.setMaxPlayers(4);

//        File islandFile = new File(islandDir, player.getUniqueId().toString() + ".yml");
//
//        if (!islandFile.exists()) {
//            try {
//                islandFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        YamlConfiguration config = YamlConfiguration.loadConfiguration(islandFile);
//
//        config.set("ownerID", player.getUniqueId().toString());
//        config.set("min", LocationUtil.serialize(min));
//        config.set("max", LocationUtil.serialize(max));
//        config.set("members", "");
//        config.set("spawn", LocationUtil.serialize(island.getSpawn()));
//        config.set("type", island.getType().toString());
//        config.set("balance", 0);
//        config.set("islandLevel", 0);
//        config.set("maxPlayers", 4);

        try {
            island.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SkyBlock.getPlugin().getSchematicLoader().pasteSchematic(type.name().toLowerCase() + ".schematic", SkyBlock.getPlugin().getIslandWorld(), center.getBlockX(), 100, center.getBlockZ());
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] Your Island is ready!");
        player.sendMessage(ChatColor.GRAY + "Type (/is home) to visit.");
        registerIsland(player.getUniqueId(), island);

        this.lastIsland = center.clone();
    }

    public Location findEmptySpace() {
        if (playerIslands.isEmpty()) {
            return new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
        }

        Location base = null;

        for (Island island : playerIslands) {

            base = island.getSpawn().clone().add(islandDistance, 0, 0);
            int i = 0;
            while (conflicts(base) && i < 500000) {
                System.out.println("Conflicts");
                base = base.clone().add(islandDistance, 0, 0);
                i++;
            }
            System.out.println("Found base: " + base.getBlockX() + ", " + base.getBlockZ());
            break;
        }
        return base;
    }

    public boolean conflicts(Island island) {
        for (Island conflict : playerIslands) {
            if (conflict.getSpawn().toVector().isInAABB(island.getContainer().getMin().toVector(), island.getContainer().getMax().toVector()) ||
                    island.getSpawn().toVector().isInAABB(conflict.getContainer().getMin().toVector(), conflict.getContainer().getMax().toVector())) {
                return true;
            }
        }
        return false;
    }

    public boolean conflicts(Location loc) {
        for (Island conflict : playerIslands) {
            if (conflict == null || conflict.getContainer() == null || loc.toVector() == null)
                return true;

            if (loc.toVector().isInAABB(conflict.getContainer().getMin().toVector(), conflict.getContainer().getMax().toVector())) {
                return true;
            }
        }
        return false;
    }

    public Island getIslandAt(Location location) {
        for (Island island : playerIslands) {
            if (location.toVector().isInAABB(island.getContainer().getMin().toVector(), island.getContainer().getMax().toVector())) {
                return island;
            }
        }
        return null;
    }

    public void deleteIsland(Player player, Island island) {

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (isInIslandRegion(island, pl.getLocation())) {
                pl.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
                pl.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] This island is being deleted! Sent you to spawn");
            }
        }

        File toDelete = new File(islandDir, island.getOwner().toString() + ".yml");

        if (!toDelete.exists()) {
            return;
        }
        toDelete.delete();


        int minX = island.getContainer().getMin().getBlockX();
        int minY = island.getContainer().getMin().getBlockY();
        int minZ = island.getContainer().getMin().getBlockZ();

        int maxX = island.getContainer().getMax().getBlockX();
        int maxY = island.getContainer().getMax().getBlockY();
        int maxZ = island.getContainer().getMax().getBlockZ();

        for (int x = minX; x < maxX; x++) {

            for (int y = minY; y < maxY; y++) {

                for (int z = minZ; z < maxZ; z++) {

                    Block block = SkyBlock.getPlugin().getIslandWorld().getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }


        SkyBlock.getPlugin().getRegionHandler().deleteRegion(island.getContainer());
        playerIslands.remove(island);
    }

    public boolean isInIslandRegion(Island island, Location loc) {
        Vector min = island.getContainer().getMin().toVector();
        Vector max = island.getContainer().getMax().toVector();

        return loc.toVector().isInAABB(min, max);
    }

    public Island getIslandForPlayer(Player player) {
        for (Island island : playerIslands) {
            if (island.getOwner().equals(player.getUniqueId()) || island.getMembers().contains(player.getUniqueId())) {
                return island;
            }
        }
        return null;
    }

    public boolean hasInvite(Player player) {
        return islandInvites.get(player.getUniqueId()) != null;
    }

    public Island getInviteFor(Player player) {
        if (hasInvite(player)) {
            return islandInvites.get(player.getUniqueId());
        }
        return null;
    }

    public Location nextIslandLocation(final Location lastIsland) {
        int d = islandDistance;
        alignToDistance(lastIsland, d);
        int x = lastIsland.getBlockX();
        int z = lastIsland.getBlockZ();
        if (x < z) {
            if (-1 * x < z) {
                x += d;
            } else {
                z += d;
            }
        } else if (x > z) {
            if (-1 * x >= z) {
                x -= d;
            } else {
                z -= d;
            }
        } else { // x == z
            if (x <= 0) {
                z += d;
            } else {
                z -= d;
            }
        }
        lastIsland.setX(x);
        lastIsland.setZ(z);
        return lastIsland;
    }

    public Map<UUID, Island> getIslandInvites() {
        return islandInvites;
    }

    public File getFileForIsland(Island island) {
        return new File(islandDir, island.getOwner().toString() + ".yml");
    }
}
