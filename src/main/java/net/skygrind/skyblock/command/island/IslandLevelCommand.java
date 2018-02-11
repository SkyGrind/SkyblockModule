package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matt on 17/04/2017.
 */

public class IslandLevelCommand extends GooseCommand {
    public IslandLevelCommand() {
        super("level", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        SkyBlock.getPlugin().getIslandWorld().setAutoSave(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                island.calculateIslandLevel();
                MessageUtil.sendServerTheme(player, ChatColor.GREEN + String.format("Your island is currently level %s.", island.getIslandLevel()));
            }
        }.runTaskAsynchronously(API.getPlugin());
        SkyBlock.getPlugin().getIslandWorld().setAutoSave(true);
    }
}
