package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.goose.GooseLocation;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IslandWarpCommand extends GooseCommand {
    public IslandWarpCommand() {
        super("warp", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /island warp <Island>");
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' was found.", args[0]));
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().findByUniqueId(target.getUniqueId());
        if (island == null) {
            sender.sendMessage(ChatColor.RED + String.format("No Island with the Owner or Member of '%s' could be found.", args[0]));
            return;
        }
        if (island.getLocked()) {
            sender.sendMessage(ChatColor.RED + String.format("The island '%s' is currently locked.", args[0]));
            return;
        }

        if (island.getWarpLocation() == null) {
            sender.sendMessage(ChatColor.RED + String.format("The Island '%s' doesn't not a warp location set.", args[0]));
            return;
        }
        sender.sendMessage(ChatColor.GREEN + String.format("You will be teleported to %s's island in 3 seconds.", args[0]));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!sender.isOnline())
                    return;

                GooseLocation gooseLocation = island.getWarpLocation();
                if (gooseLocation == null) {
                    sender.sendMessage(ChatColor.GREEN + "The Island '%s' no longer has a warp location set.");
                    return;
                }

                sender.teleport(gooseLocation.toLocation());
                sender.sendMessage(ChatColor.GREEN + String.format("You have been teleported to %s's island.", args[0]));
            }
        }.runTaskLater(API.getPlugin(), 3 * 20L);
    }
}
