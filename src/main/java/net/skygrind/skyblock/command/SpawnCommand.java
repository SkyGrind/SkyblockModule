package net.skygrind.skyblock.command;

import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matt on 2017-02-25.
 */
public class SpawnCommand extends BukkitCommand {

    public SpawnCommand() {
        super("spawn");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;

        MessageUtil.sendServerTheme(player, ChatColor.GREEN + "You are being teleported to spawn in 3 seconds....");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            }
        }.runTaskLater(API.getPlugin(), 3 * 20L);
        return true;
    }
}
