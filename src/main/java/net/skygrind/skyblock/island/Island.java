package net.skygrind.skyblock.island;

import com.google.gson.GsonBuilder;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseLocation;
import net.skygrind.skyblock.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Matt on 2017-02-11.
 */
public class Island implements Comparable<Island> {
    private GooseLocation spawn;
    private Region container;
    private UUID owner;
    private Integer maxPlayers;
    private List<UUID> members;
    private Integer size;
    private IslandType type;
    private String islandName;
    private Integer islandLevel = 0;
    private Integer bankBalance = 0;
    private Boolean locked;
    private GooseLocation warpLocation;
    private List<UUID> expelled;

    private List<UUID> coop = new ArrayList<>();

    public Island(UUID owner, Location spawn, IslandType type) {
        this.owner = owner;
        this.spawn = GooseLocation.fromLocation(spawn);
        this.type = type;

        if (Bukkit.getPlayer(owner) == null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
            islandName = player.getName();
        } else {
            this.islandName = Bukkit.getPlayer(owner).getName();
        }
        this.locked = false;
        this.warpLocation = null;


        this.expelled = new ArrayList<>();
    }

    public int getIslandLevel() {
        return islandLevel;
    }

    public void setIslandLevel(int islandLevel) {
        this.islandLevel = islandLevel;
    }

    public Location getSpawn() {
        return spawn.toLocation();
    }

    public void setSpawn(final Location location) {
        this.spawn = GooseLocation.fromLocation(location);
    }

    public Region getContainer() {
        return container;
    }

    public void setContainer(Region container) {
        this.container = container;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public List<UUID> getExpelled() {
        return expelled;
    }

    public void setExpelled(List<UUID> expelled) {
        this.expelled = expelled;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public IslandType getType() {
        return type;
    }

    public void setType(IslandType type) {
        this.type = type;
    }

    public List<UUID> getCoop() {
        return coop;
    }

    public void setIslandName(String name) {
        this.islandName = name;
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid) || owner.equals(uuid);
    }

    public boolean isAllowed(UUID uuid) {
        return members.contains(uuid) || owner.equals(uuid) || coop.contains(uuid);
    }

    public boolean isExpelled(UUID uuid) {
        if (expelled == null)
            expelled = new ArrayList<>();

        return expelled.contains(uuid);
    }

    public String getName() {
        if (Bukkit.getPlayer(owner) == null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
            islandName = player.getName();
        } else {
            this.islandName = Bukkit.getPlayer(owner).getName();
        }

        return islandName + "'s Island";
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(int islandBank) {
        this.bankBalance = islandBank;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void save() {
        File file = new File(SkyBlock.getPlugin().getModuleDir().toString() + File.separator + "islands" + File.separator + owner.toString().replace("-", "") + ".json");
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);

        try (FileWriter writer = new FileWriter(file.toString())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public GooseLocation getWarpLocation() {
        return warpLocation;
    }

    public void setWarpLocation(GooseLocation warpLocation) {
        this.warpLocation = warpLocation;
    }

    private boolean isIslandLevelBlock(Block block) {
        Material type = block.getType();

        return type == Material.IRON_BLOCK
                || type == Material.GOLD_BLOCK
                || type == Material.LAPIS_BLOCK
                || type == Material.REDSTONE_BLOCK
                || type == Material.DIAMOND_BLOCK
                || type == Material.COAL_BLOCK
                || type == Material.EMERALD_BLOCK;
    }

    public void calculateIslandLevel() {
        Location min = this.container.getMin();
        Location max = this.container.getMax();

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();

        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

        List<Block> blocks = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = minZ; z < maxZ; z++) {

                    Block block = SkyBlock.getPlugin().getIslandWorld().getBlockAt(x, y, z);
                    if (block.getType().equals(Material.BARRIER))
                        block.setType(Material.AIR);

                    if (isIslandLevelBlock(block)) {
                        blocks.add(block);
                    }
                }
            }
        }
        setIslandLevel(handleBlock(blocks));
    }

    private int handleBlock(List<Block> blocks) {
        double islandLevel = 0;

        for (Block block : blocks) {

            switch (block.getType()) {

                case LAPIS_BLOCK:
                    islandLevel += 0.25;
                    break;
                case GOLD_BLOCK:
                    islandLevel += 0.5;
                    break;
                case IRON_BLOCK:
                    islandLevel += 0.5;
                    break;
                case REDSTONE_BLOCK:
                    islandLevel += 0.25;
                    break;
                case DIAMOND_BLOCK:
                    islandLevel += 0.75;
                    break;
                case COAL_BLOCK:
                    islandLevel += 0.5;
                    break;
                case EMERALD_BLOCK:
                    islandLevel += 1.0;
                    break;
            }
        }
        return (int) Math.ceil(islandLevel);
    }

    @Override
    public int compareTo(Island island) {
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(spawn, container, owner, maxPlayers, members, size, expelled, type, islandName, islandLevel, bankBalance, locked, warpLocation);
    }
}
