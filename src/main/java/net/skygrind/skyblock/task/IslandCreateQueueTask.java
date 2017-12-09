package net.skygrind.skyblock.task;

import com.islesmc.modules.api.task.Task;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandType;
import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class IslandCreateQueueTask extends BukkitRunnable {
    private final Queue<QueueItem> islandQueue;

    public IslandCreateQueueTask() {
        this.islandQueue = new PriorityQueue<>();
    }

    public void queueIsland(final Player player, final IslandType type) {
        this.islandQueue.add(new QueueItem(player, type));
    }

    @Override
    public void run() {
        for (QueueItem item : islandQueue) {
            Player player = item.getPlayer();
            Location center = SkyBlock.getPlugin().getIslandRegistry().nextIslandLocation(SkyBlock.getPlugin().getIslandRegistry().getLastIsland());
            if (center == null) {
                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
            }

            Island island = new Island(player.getUniqueId(), center, item.getType());
            island.setSize(SkyBlock.getPlugin().getIslandRegistry().getBaseIslandSize());

            double minX = center.getBlockX() - island.getType().getSize() / 2;
            double minY = 0;
            double minZ = center.getBlockZ() - island.getType().getSize() / 2;

            double maxX = center.getBlockX() + island.getType().getSize() / 2;
            double maxY = 256;
            double maxZ = center.getBlockZ() + island.getType().getSize() / 2;


            Location min = new Location(SkyBlock.getPlugin().getIslandWorld(), minX, minY, minZ);
            Location max = new Location(SkyBlock.getPlugin().getIslandWorld(), maxX, maxY, maxZ);

            Region container = SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max);
            island.setContainer(container);
            island.setMembers(new ArrayList<>());
            island.setIslandLevel(0);

            island.setMaxPlayers(4);

            island.save();

            try {
                SkyBlock.getPlugin().getSchematicLoader().pasteSchematic(item.type.name().toLowerCase() + ".schematic", SkyBlock.getPlugin().getIslandWorld(), center.getBlockX(), 100, center.getBlockZ());
            } catch (DataException | IOException | MaxChangedBlocksException e) {
                e.printStackTrace();
            }

            MessageUtil.sendServerTheme(player, ChatColor.GREEN + "Your island has been created.");
            player.sendMessage(ChatColor.GREEN + "Type (/is home) to visit.");
            SkyBlock.getPlugin().getIslandRegistry().registerIsland(player.getUniqueId(), island);
            this.islandQueue.remove(item);
            SkyBlock.getPlugin().getIslandRegistry().setLastIsland(center);
        }
    }

    private class QueueItem {
        private final Player player;
        private final IslandType type;

        public QueueItem(final Player player, final IslandType type) {
            this.player = player;
            this.type = type;
        }

        public Player getPlayer() {
            return player;
        }

        public IslandType getType() {
            return type;
        }
    }
}
