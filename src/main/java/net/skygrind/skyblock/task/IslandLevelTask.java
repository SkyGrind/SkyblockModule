package net.skygrind.skyblock.task;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import org.bukkit.scheduler.BukkitRunnable;

public class IslandLevelTask extends BukkitRunnable {
    @Override
    public void run() {
        SkyBlock.getPlugin().getIslandRegistry().getPlayerIslands().forEach(Island::calculateIslandLevel);
    }
}
