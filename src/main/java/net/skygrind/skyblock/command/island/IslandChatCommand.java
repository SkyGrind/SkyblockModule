package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.ChatMode;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.listeners.GeneralListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IslandChatCommand extends GooseCommand {
    public IslandChatCommand() {
        super("chat", Lists.newArrayList("c"), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /island chat <island/Public>");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(sender);
        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You must be in an island to execute this command.");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "is":
            case "i":
            case "team":    
            case "island": {
                GeneralListener.CHAT_MODE_MAP.put(sender.getUniqueId(), ChatMode.ISLAND);
                sender.sendMessage(ChatColor.YELLOW + "You are now speaking in " + ChatColor.GREEN + "Island " + ChatColor.YELLOW + "chat.");
                break;
            }
            case "g":
            case "global":
            case "public":
            case "p": {
                GeneralListener.CHAT_MODE_MAP.put(sender.getUniqueId(), ChatMode.PUBLIC);
                sender.sendMessage(ChatColor.YELLOW + "You are now speaking in " + ChatColor.GREEN + "Public " + ChatColor.YELLOW + "chat.");
                break;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "Usage: /island chat <island/Public>");
                break;
            }
        }
    }
}
