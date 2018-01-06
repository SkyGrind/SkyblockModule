package net.skygrind.skyblock.task;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.configuration.ServerType;
import net.skygrind.skyblock.goose.GooseLocation;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandType;
import net.skygrind.skyblock.misc.LocationUtil;
import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sun.applet.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class IslandCreateQueueTask extends BukkitRunnable {
    private final Queue<QueueItem> islandQueue;

    public IslandCreateQueueTask() {
        this.islandQueue = new PriorityQueue<>((queueItem, t1) -> 0);
    }

    public void queueIsland(final Player player, final IslandType type) {
        this.islandQueue.add(new QueueItem(player, type));
    }

    @Override
    public void run() {
        for (QueueItem item : islandQueue) {

            if (item == null || item.getPlayer() == null || item.getType() == null) {
                continue;
            }

//            int islandNumber = SkyBlock.getPlugin().getServerConfig().getIslandsEverCreated().incrementAndGet();
//            int prime = SkyBlock.getPlugin().getGridUtil().getInts()[islandNumber];
//
//            int xZ = prime * 1000;

            Location center = getLocation();

            Player player = item.getPlayer();

//            Location center;
//
//            if (SkyBlock.getPlugin().getIslandRegistry().getLastIsland() == null) {
//                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
//            }
//            else {
//                center = SkyBlock.getPlugin().getIslandRegistry().nextIslandLocation(SkyBlock.getPlugin().getIslandRegistry().getLastIsland());
//            }

            if (center == null) {
                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
            }

            if (center.getWorld() == null || center.getWorld().getName().equalsIgnoreCase("world")) {
                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
                System.out.println("center null again");
            }

            // For some reason this fixes it??
            center = center.clone().add(0, 0, 0);
            System.out.println(String.format("LOCATION SERIALIZED IS %s", LocationUtil.serialize(center)));

            Island island = new Island(player.getUniqueId(), center, item.getType());
            island.setSize(SkyBlock.getPlugin().getIslandRegistry().getBaseIslandSize());

            double minX = center.getBlockX() - island.getType().getSize() / 2D;
            double minY = 0D;
            double minZ = center.getBlockZ() - island.getType().getSize() / 2D;

            double maxX = center.getBlockX() + island.getType().getSize() / 2D;
            double maxY = 256D;
            double maxZ = center.getBlockZ() + island.getType().getSize() / 2D;


            Location min = new Location(SkyBlock.getPlugin().getIslandWorld(), minX, minY, minZ);
            Location max = new Location(SkyBlock.getPlugin().getIslandWorld(), maxX, maxY, maxZ);

            Region container = SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max);
            island.setContainer(container);
            island.setMembers(new ArrayList<>());
            island.setIslandLevel(0);


            int maxPlayers = 4;
            if (SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES)
                maxPlayers = 5;

            island.setMaxPlayers(maxPlayers);

            try {
                SkyBlock.getPlugin().getSchematicLoader().pasteSchematic(item.type.name().toLowerCase() + ".schematic", SkyBlock.getPlugin().getIslandWorld(), center.getBlockX(), 100, center.getBlockZ());
            } catch (DataException | IOException | MaxChangedBlocksException e) {
                e.printStackTrace();
            }

            if (item.getType() == IslandType.DEFAULT) {
                center = center.clone().add(4, 8, 6);
                System.out.println("default island");
            }
            center = center.getWorld().getHighestBlockAt(center).getLocation();
            island.setSpawn(center);
            island.save();

            MessageUtil.sendServerTheme(player, ChatColor.GREEN + "Your island has been created.");
            player.sendMessage(ChatColor.GREEN + "Type (/is home) to visit.");
            SkyBlock.getPlugin().getIslandRegistry().registerIsland(player.getUniqueId(), island);
            this.islandQueue.remove(item);
            SkyBlock.getPlugin().getIslandRegistry().setLastIsland(center);
            SkyBlock.getPlugin().getServerConfig().setLastIslandLocation(GooseLocation.fromLocation(center));
            SkyBlock.getPlugin().getServerConfig().save();
        }
    }

    private Location getLocation() {
        GooseLocation last = SkyBlock.getPlugin().getServerConfig().getLastIslandLocation();
        if (last == null) {
            last = GooseLocation.fromLocation(new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 0, 0));
        }
        Location next = SkyBlock.getPlugin().getIslandRegistry().getNextLocation(last.toLocation());
        while (SkyBlock.getPlugin().getIslandRegistry().getIslandAt(next) != null) {
            next = SkyBlock.getPlugin().getIslandRegistry().getNextLocation(next);
        }
        return next;
    }

    private class QueueItem implements Comparable<QueueItem> {
        private final Player player;
        private final IslandType type;

        QueueItem(final Player player, final IslandType type) {
            this.player = player;
            this.type = type;
        }

        Player getPlayer() {
            return player;
        }

        IslandType getType() {
            return type;
        }

        @Override
        public int compareTo(QueueItem o) {
            return 0;
        }
    }
}
