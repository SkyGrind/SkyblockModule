package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IslandExpelCommand extends GooseCommand {
    
    public IslandExpelCommand() {
        super("expel", Lists.newArrayList("ban"), true);
    }
    
    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "You must expel a player: /is expel <player>");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            player.sendMessage(ChatColor.RED + " Requested player could not be found!");
            return;
        }
        
        handleExpell(player, target);
       
    }
    
    private void handleExpell(Player owner, Player player) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(owner);
        
        if (!island.getOwner().equals(owner.getUniqueId())) {
            owner.sendMessage(ChatColor.RED + "You cannot expel a player!");
            return;
        }
        
        if (island.isMember(player.getUniqueId())) {
            owner.sendMessage(ChatColor.RED + "You cannot expel a member of your own island.");
            return;
        }
        
        if (island.getExpelled().contains(player.getUniqueId())) {
            owner.sendMessage(ChatColor.GREEN + "You have un-expelled " + player.getName());
            player.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "You have been unbanned from " + island.getName() + "'s island");
            island.getExpelled().remove(player.getUniqueId());
            return;
        }
        
        island.getExpelled().add(player.getUniqueId());
        owner.sendMessage(ChatColor.GREEN + "You have expelled " + player.getName());
        player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "You have been banned from " + island.getName() + "'s island");
        
        Island currentIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(player.getLocation());
        
        if (currentIsland == null || currentIsland != island) {
            return;
        }
        
        player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        player.sendMessage(ChatColor.RED + "You can no longer be on that island");
    }
}
