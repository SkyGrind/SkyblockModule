package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IslandForceJoin extends GooseCommand {

    public IslandForceJoin() {
        super("force", Lists.newArrayList("join"), true);
    }

    @Override
    public void execute(Player pLayer, String[] args) {
        if (args.length != 1) {
            pLayer.sendMessage(ChatColor.RED + "/is force <playerInIsland>");
            return;
        }

        if (!pLayer.hasPermission("island.force.join")) {
            pLayer.sendMessage(ChatColor.RED + "You do not have permission for this!");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(pLayer);

        if (island != null) {
            pLayer.sendMessage(ChatColor.RED + "You have to leave your current island first");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            pLayer.sendMessage(ChatColor.RED + "Requested player could not be found");
            return;
        }

        Island targetIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(target);

        if (targetIsland == null) {
            pLayer.sendMessage(ChatColor.RED + "Player does not currently have an island");
            return;
        }

        targetIsland.getMembers().add(pLayer.getUniqueId());
        targetIsland.setOwner(pLayer.getUniqueId());

        pLayer.sendMessage(ChatColor.GREEN + "You have forced joined " + targetIsland.getName());

    }
}
