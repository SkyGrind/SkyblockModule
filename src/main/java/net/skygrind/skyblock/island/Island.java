package net.skygrind.skyblock.island;

import com.google.gson.GsonBuilder;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseLocation;
import net.skygrind.skyblock.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        this.islandName = islandName;
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

    public void setSpawn(final Location location) {
        this.spawn = GooseLocation.fromLocation(location);
    }

    public void save() {
        File file = new File(SkyBlock.getPlugin().getModuleDir().toString() + File.separator + "islands" + File.separator + owner.toString().replace("-", "") + ".json");
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);

        try (FileWriter writer = new FileWriter(file.toString())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(json);
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

    @Override
    public int compareTo(Island island) {
        return 0;
    }
}
