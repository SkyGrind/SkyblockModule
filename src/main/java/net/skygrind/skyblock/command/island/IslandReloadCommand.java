package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IslandReloadCommand extends GooseCommand {
    public IslandReloadCommand() {
        super("reload", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (!sender.hasPermission("skyblock.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return;
        }
        SkyBlock.getPlugin().getOreGenerationConfig().loadValues();
        sender.sendMessage(ChatColor.YELLOW + "You have successfully reloaded the ore generation configuration.");
    }
}
