package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.commands.SubCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandKickCommand extends SubCommand {
    private final IslandRegistry registry;

    public IslandKickCommand() {
        super("kick", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/island kick [Player]");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have an island!");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You cannot kick an offline player!");
            return;
        }

        if (!island.getOwner().equals(player.getUniqueId()) && !player.hasPermission("skygrind.skyblock.kick")) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to do this!");
            return;
        }

        if (!island.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "This player is not in your island!");
            return;
        }

        island.getMembers().remove(target.getUniqueId());
        if (registry.isInIslandRegion(island, target.getLocation())) {
            target.teleport(SkyBlock.getPlugin().getSpawn());
        }
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You have kicked " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " off your island!");
        target.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You have been kicked off your island!");
        return;
    }
}
