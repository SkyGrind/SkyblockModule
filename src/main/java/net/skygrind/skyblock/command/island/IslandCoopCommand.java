package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class IslandCoopCommand extends GooseCommand {

    private IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    public IslandCoopCommand() {
        super("coop", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length == 0) {
            return;
        }

        Island island = registry.getIslandForPlayer(sender);

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You do not have an island to do this!");
            return;
        }

        if (args.length != 2) {
            return;
        }

        String playerName = args[1];

        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "That player is offline or does not exist!");
            return;
        }


        if (args[0].equalsIgnoreCase("add")) {
            if (island.getCoop().contains(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "This player is already CO-OP on your island!");
                return;
            }

            island.getCoop().add(player.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "Successfully added " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " as a CO-OP player to your island!");
            player.sendMessage(ChatColor.GREEN + "You have been added as a CO-OP player to " + ChatColor.GOLD + sender.getName() + ChatColor.RED + "'s island!");
            return;
        }
        else if (args[0].equalsIgnoreCase("remove")) {
            if (!island.getCoop().contains(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "This player is not CO-OP on your island!");
                return;
            }

            island.getCoop().remove(player.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "Successfully removed " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " as a CO-OP player from your island!");
            player.sendMessage(ChatColor.RED + "You have been removed as a CO-OP player from " + ChatColor.GOLD + sender.getName() + ChatColor.RED + "'s island!");
            return;
        }
    }
}
