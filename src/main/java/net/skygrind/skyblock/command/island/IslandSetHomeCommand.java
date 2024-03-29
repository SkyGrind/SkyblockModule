package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IslandSetHomeCommand extends GooseCommand {

    public IslandSetHomeCommand() {
        super("sethome", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island sethome");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + "You must have an island to execute this command.");
            return;
        }

        int x = Math.round(player.getLocation().getBlockX());
        int y = Math.round(player.getLocation().getBlockY());
        int z = Math.round(player.getLocation().getBlockZ());

        island.setSpawn(player.getLocation());

        player.sendMessage(ChatColor.GREEN + String.format("You have set your island's home to (%s, %s, %s)", x, y, z));
        for (UUID uuid : island.getMembers()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member != null)
                member.sendMessage(ChatColor.GREEN + String.format("%s has set your island's home to (%s, %s, %s)", player.getName(), x, y, z));
        }
        return;
    }
}
