package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandDeleteCommand extends GooseCommand implements Listener {
    private final IslandRegistry registry;

    public IslandDeleteCommand() {
        super("delete", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();
    }


    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island delete");
        }

        Island island = registry.getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + "You do not have an island to delete.");
            return;
        }

        if (!island.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Only the owner of your island can delete it.");
            return;
        }

        registry.deleteIsland(player, island);
        MessageUtil.sendServerTheme(player, "You have successfully deleted your island.");
        return;
    }
}
