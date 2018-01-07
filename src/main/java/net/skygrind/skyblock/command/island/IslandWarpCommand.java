package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.configuration.ServerType;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.goose.GooseLocation;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("--force")) {
                    if (!sender.hasPermission("island.warp.force")) {
                        sender.sendMessage(ChatColor.RED + "Usage: /island warp <Island>");
                        return;
                    }
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' was found.", args[0]));
                        return;
                    }
                    doWarp(sender, target, true);
                }
                return;
            }
            sender.sendMessage(ChatColor.RED + "Usage: /island warp <Island>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' was found.", args[0]));
            return;
        }

        if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
            sender.sendMessage(ChatColor.RED + "You cannot warp of other islands on this realm!");
            return;
        }

        doWarp(sender, target, false);
    }

    private void doWarp(final Player sender, final OfflinePlayer target, final boolean force) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().findByUniqueId(target.getUniqueId());
        if (island == null) {
            sender.sendMessage(ChatColor.RED + String.format("No Island with the Owner or Member of '%s' could be found.", target.getName()));
            return;
        }
        if (island.getLocked()) {
            sender.sendMessage(ChatColor.RED + String.format("The island '%s' is currently locked.", target.getName()));
            return;
        }
        GooseLocation location = island.getWarpLocation();
        if (location == null) {
            if (force) {
                location = GooseLocation.fromLocation(island.getSpawn());
            } else {
                sender.sendMessage(ChatColor.RED + String.format("The Island '%s' doesn't not a warp location set.", target.getName()));
                return;
            }
        }
        sender.sendMessage(ChatColor.GREEN + String.format("You will be teleported to %s's island in 3 seconds.", target.getName()));
        GooseLocation finalLocation = location;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!sender.isOnline())
                    return;

                sender.teleport(finalLocation.toLocation());
                sender.sendMessage(ChatColor.GREEN + String.format("You have been teleported to %s's island.", target.getName()));
            }
        }.runTaskLater(API.getPlugin(), 3 * 20L);
    }
}
