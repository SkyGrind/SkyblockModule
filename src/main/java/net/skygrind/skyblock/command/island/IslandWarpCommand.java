package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer()
    }
}
