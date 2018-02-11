package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.configuration.ServerType;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IslandLockCommand extends GooseCommand {
    public IslandLockCommand() {
        super("lock", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES) {
            sender.sendMessage(ChatColor.RED + "That cannot be used on this realm!");
            return;
        }
        
        if (args.length > 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /island lock");
            return;
        }
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(sender);

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        if (island.getLocked()) {
            island.setLocked(false);
            MessageUtil.sendServerTheme(sender, ChatColor.GREEN + "Your island is no longer locked; anybody can warp to your island.");
            return;
        }
        island.setLocked(true);
        MessageUtil.sendServerTheme(sender, ChatColor.GREEN + "Your island is now locked; only members can warp to your island.");
    }
}
