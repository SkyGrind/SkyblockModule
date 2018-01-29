package net.skygrind.skyblock.island.listeners;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.configuration.ServerType;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Matt on 2017-02-25.
 */
public class IslandListener implements Listener {
    private final IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();
    private final List<Material> canMine;

    public IslandListener() {
        this.canMine = new ArrayList<>();
        this.canMine.add(Material.EMERALD_BLOCK);
        this.canMine.add(Material.IRON_BLOCK);
        this.canMine.add(Material.DIAMOND_BLOCK);
        this.canMine.add(Material.GOLD_BLOCK);
        this.canMine.add(Material.LAPIS_BLOCK);
        this.canMine.add(Material.REDSTONE_BLOCK);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placer = event.getPlayer();

        if (placer.hasPermission("skyblock.bypass"))
            return;

        Location location = event.getBlockPlaced().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island conflict = registry.getIslandAt(location);

        if (conflict == null) {
            if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
                return;
            }
            event.setCancelled(true);
            if (location.getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName())) {
                placer.sendMessage(ChatColor.GREEN + "You cannot place outside of your island.");
                MessageUtil.sendServerTheme(placer, ChatColor.GREEN + "To upgrade your islands size visit: http://shop.skyparadisemc.com");
            }
            return;
        }

        if (!conflict.isMember(placer.getUniqueId())) {
            if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES) && (conflict.getIslandLevel() >= 5)) {
                return;
            }
            placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        System.out.println("sign update event");
        Location location = event.getBlock().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island island = registry.getIslandAt(location);
        if (island == null)
            return;

        if (!event.getLine(0).equalsIgnoreCase("[welcome]"))
            return;

        island.setWarpLocation(GooseLocation.fromLocation(location));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlock().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld())
            return;

        if (placer.hasPermission("skyblock.bypass"))
            return;

        Island conflict = registry.getIslandAt(location);
        if (conflict == null)
            return;

        if (conflict.isMember(placer.getUniqueId()))
            return;


        System.out.println(String.format("conflicting w/ %s & !member", conflict.getName()));

        if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES) && (conflict.getIslandLevel() < 5)) {
            // Can raid
            System.out.println("can raid");
            if (!this.canMine.contains(event.getBlock().getType())) {
                System.out.println(String.format("%s cannot mine", placer.getName()));
                placer.sendMessage(ChatColor.RED + "You cannot mine this block while raiding; you must blow it up.");
                event.setCancelled(true);
                return;
            }
            return;
        }

        if (event.getBlock().getType().equals(Material.SIGN) || event.getBlock().getType().equals(Material.SIGN_POST) || event.getBlock().getType().equals(Material.WALL_SIGN)) {
            Sign sign = (Sign) event.getBlock().getState();

            if (sign.getLine(0).equalsIgnoreCase("[welcome]"))
                conflict.setWarpLocation(null);

        }

        System.out.println(String.format("%s cannot mind", placer.getName()));
        placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            //Added to allow SkyBattles to work. -IcyRelic
            if (block.getWorld() != SkyBlock.getPlugin().getIslandWorld())
                return;

            if (!(block.getType() == Material.CHEST)) {
                return;
            }

            if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
                return;
            }

            if (registry.conflicts(block.getLocation())) {

                Island conflict = registry.getIslandAt(block.getLocation());

                if (!conflict.getMembers().contains(player.getUniqueId()) && !conflict.getOwner().equals(player.getUniqueId()) && !player.hasPermission("skyblock.bypass")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to open containers here!");
                    event.setCancelled(true);
                }
            }
        }
    }
}