package net.skygrind.skyblock.island.listeners;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 * Created by Matt on 2017-02-25.
 */
public class IslandListener implements Listener {

    private IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlockPlaced().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        if (registry.conflicts(location)) {

            Island conflict = registry.getIslandAt(location);

            if (!conflict.getMembers().contains(placer.getUniqueId()) && !conflict.getOwner().equals(placer.getUniqueId())) {

                if (placer.hasPermission("skyblock.bypass")) {
                    return;
                }

                placer.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to build here!");
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
            if (location.getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName())) {
                MessageUtil.sendUrgent(placer, ChatColor.RED + "This is out of your islands region!");
                placer.sendMessage(ChatColor.GREEN + "You cannot place outside of your island.");
                MessageUtil.sendServerTheme(placer, ChatColor.GREEN + "To upgrade your islands size visit: http://shop.skyparadisemc.com");
            } else {

                if (!placer.hasPermission("islesmc.build")) {
                    placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlock().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        if (registry.conflicts(location)) {

            Island conflict = registry.getIslandAt(location);

            if (!conflict.getMembers().contains(placer.getUniqueId()) && !conflict.getOwner().equals(placer.getUniqueId())) {

                if (placer.hasPermission("skyblock.bypass")) {
                    return;
                }

                placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            //Added to allow SkyBattles to work. -IcyRelic
            if (block.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;


            if (!(block.getType() == Material.CHEST)) {
                return;
            }

            if (registry.conflicts(block.getLocation())) {

                Island conflict = registry.getIslandAt(block.getLocation());

                if (!conflict.getMembers().contains(player.getUniqueId()) && !conflict.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to open containers here!");
                    event.setCancelled(true);
                }
            }
        }
    }
}