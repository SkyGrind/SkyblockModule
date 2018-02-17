package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandKickCommand extends GooseCommand {

    public IslandKickCommand() {
        super("kick", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /island kick <Player>");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + String.format("'%s' Could not be found.", args[0]));
            return;
        }

        if (!island.getOwner().equals(player.getUniqueId()) && !player.hasPermission("islesmc.staff.kick")) {
            player.sendMessage(ChatColor.RED + "Only the island's owner can kick members.");
            return;
        }

        if (!island.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.RED + String.format("'%s' is not currently a member of your island.", player.getName()));
            return;
        }
        
        if (target.isOnline()) {
            Player targetOnline = Bukkit.getPlayer(target.getUniqueId());
            island.getMembers().remove(targetOnline.getUniqueId());
            if (SkyBlock.getPlugin().getIslandRegistry().isInIslandRegion(island, targetOnline.getLocation())) {
                targetOnline.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
                MessageUtil.sendServerTheme(targetOnline, ChatColor.RED + String.format("You have been kicked from %s's island.", player.getName()));
                return;
            }
        }
        island.getMembers().remove(target.getUniqueId());
        MessageUtil.sendServerTheme(player, ChatColor.GREEN + String.format("You have kicked %s from your island.", target.getName()));
        
    }
}
