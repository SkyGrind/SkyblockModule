package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IslandNameCommand extends GooseCommand {

    public IslandNameCommand() {
        super("name", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/island name [name]");
            return;
        }

        String name = args[0];
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(sender);

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You do not have an island to name!");
            return;
        }

        if (island.getOwner() != sender.getUniqueId()) {
            sender.sendMessage(ChatColor.RED + "You are not the island owner!");
            return;
        }

        island.setIslandName(name);
        sender.sendMessage(ChatColor.GREEN + "Set your island name to: " + ChatColor.GOLD + name);
    }
}
