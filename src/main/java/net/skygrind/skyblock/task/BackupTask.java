package net.skygrind.skyblock.task;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BackupTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.broadcastMessage(ChatColor.RED + "[Backup] We're backing up the server, we apologize for any lag.");
        SkyBlock.getPlugin().getIslandWorld().save();
        SkyBlock.getPlugin().getIslandRegistry().getPlayerIslands().forEach(Island::save);
        SkyBlock.getPlugin().getServerConfig().save();
        Bukkit.broadcastMessage(ChatColor.RED + "[Backup] We have successfully saved all of our data.");
    }
}
