package net.skygrind.skyblock.island.listeners;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseLocation;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
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

            if (!conflict.isMember(placer.getUniqueId())) {

                if (placer.hasPermission("skyblock.bypass")) {
                    return;
                }

                placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
                event.setCancelled(true);
                return;
            }
        } else {
            event.setCancelled(true);
            if (location.getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName())) {
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
    public void onSignChange(final SignChangeEvent event) {
        Location location = event.getBlock().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island island = registry.getIslandAt(location);
        if (island == null)
            return;

        if (!event.getLine(1).equalsIgnoreCase("[welcome]"))
            return;

        island.setWarpLocation(GooseLocation.fromLocation(location));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlock().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        if (registry.conflicts(location)) {

            Island conflict = registry.getIslandAt(location);

            if (!conflict.isMember(placer.getUniqueId())) {

                if (placer.hasPermission("skyblock.bypass")) {
                    return;
                }

                placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
                event.setCancelled(true);
                return;
            }

            if (event.getBlock() instanceof Sign) {
                Sign sign = (Sign)event.getBlock();
                if (!sign.getLine(1).equalsIgnoreCase("[welcome]"))
                    return;

                conflict.setWarpLocation(null);
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