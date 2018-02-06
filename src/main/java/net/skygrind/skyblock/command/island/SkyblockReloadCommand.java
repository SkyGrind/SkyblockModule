package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.goose.GooseCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SkyblockReloadCommand extends GooseCommand {
    public SkyblockReloadCommand() {
        super("reload", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (!sender.hasPermission("skyblock.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return;
        }

    }
}
