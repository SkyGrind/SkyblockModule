package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 17/04/2017.
 */

public class IslandLevelCommand extends GooseCommand {
    public IslandLevelCommand() {
        super("level", Lists.newArrayList(), true);
    }

//    private boolean isIslandLevelBlock(Block block) {
//        Material type = block.getType();
//
//        return type == Material.IRON_BLOCK
//                || type == Material.GOLD_BLOCK
//                || type == Material.LAPIS_BLOCK
//                || type == Material.REDSTONE_BLOCK
//                || type == Material.DIAMOND_BLOCK
//                || type == Material.COAL_BLOCK
//                || type == Material.EMERALD_BLOCK;
//    }

//    private int handleBlock(List<Block> blocks) {
//        double islandLevel = 0;
//
//        for (Block block : blocks) {
//
//            switch (block.getType()) {
//
//                case LAPIS_BLOCK:
//                    islandLevel += 0.25;
//                    break;
//                case GOLD_BLOCK:
//                    islandLevel += 0.5;
//                    break;
//                case IRON_BLOCK:
//                    islandLevel += 0.5;
//                    break;
//                case REDSTONE_BLOCK:
//                    islandLevel += 0.25;
//                    break;
//                case DIAMOND_BLOCK:
//                    islandLevel += 0.75;
//                    break;
//                case COAL_BLOCK:
//                    islandLevel += 0.5;
//                    break;
//                case EMERALD_BLOCK:
//                    islandLevel += 1.0;
//                    break;
//            }
//        }
//        return (int) Math.ceil(islandLevel);
//    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

//        Region region = island.getContainer();
//
//        Location min = region.getMin();
//        Location max = region.getMax();
//
//        int minX = min.getBlockX();
//        int minY = min.getBlockY();
//        int minZ = min.getBlockZ();
//
//        int maxX = max.getBlockX();
//        int maxY = max.getBlockY();
//        int maxZ = max.getBlockZ();
//
//        List<Block> blocks = new ArrayList<>();
//
//        for (int x = minX; x < maxX; x++) {
//            for (int y = 0; y < 256; y++) {
//                for (int z = minZ; z < maxZ; z++) {
//
//                    Block block = SkyBlock.getPlugin().getIslandWorld().getBlockAt(x, y, z);
//                    if (isIslandLevelBlock(block)) {
//                        blocks.add(block);
//                    }
//                }
//            }
//        }

        SkyBlock.getPlugin().getIslandWorld().setAutoSave(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                island.calculateIslandLevel();
                MessageUtil.sendServerTheme(player, ChatColor.GREEN + String.format("Your island is currently level %s.", island.getIslandLevel()));
            }
        }.runTaskAsynchronously(API.getPlugin());
        SkyBlock.getPlugin().getIslandWorld().setAutoSave(true);
    }
}
