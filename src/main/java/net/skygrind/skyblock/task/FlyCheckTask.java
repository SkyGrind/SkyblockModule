package net.skygrind.skyblock.task;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyCheckTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("skyparadise.staff"))
                return;

            Location location = player.getLocation();
            Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
            if (island == null) {
                disableFlight(player);
                return;
            }

            if (island.getMembers().contains(player.getUniqueId()))
                return;

            disableFlight(player);
        }
    }

    private void disableFlight(final Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.RED + "You are only allowed to fly in your island.");
        }
    }
}
