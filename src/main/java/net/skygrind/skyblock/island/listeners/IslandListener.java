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
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Matt on 2017-02-25.
 */
public class IslandListener implements Listener {
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

        if (placer.getItemInHand().getType().equals(Material.BARRIER)) {
            placer.setItemInHand(new ItemStack(Material.AIR));
            event.setBuild(false);
        }

        if (placer.hasPermission("skyblock.bypass"))
            return;

        Location location = event.getBlockPlaced().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island conflict = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);

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

        if (!conflict.isAllowed(placer.getUniqueId())) {
            placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
       // System.out.println("sign update event");
        Location location = event.getBlock().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
        if (island == null)
            return;

        if (!event.getLine(0).equalsIgnoreCase("[welcome]"))
            return;

        island.setWarpLocation(GooseLocation.fromLocation(location));
        event.setLine(0, ChatColor.GREEN + "[Welcome]");
    }

    @EventHandler
    public void onBucketPlace(final PlayerBucketEmptyEvent event) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getBlockClicked().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getBlockClicked().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
            Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getTo());
            if (island == null)
                return;

            if (island.isAllowed(event.getPlayer().getUniqueId()))
                return;

            if (!island.getLocked())
                return;

            if (!island.isExpelled(event.getPlayer().getUniqueId()))
                return;

            event.setTo(event.getFrom());
            event.getPlayer().sendMessage(ChatColor.RED + "This island is currently locked.");
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        Island to = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getTo());
        if (to == null)
            return;

        if (to.isAllowed(event.getPlayer().getUniqueId()))
            return;

        if (!to.isExpelled(event.getPlayer().getUniqueId()))
            return;


        if (!to.getLocked())
            return;

        event.setTo(event.getFrom());
        event.getPlayer().sendMessage(ChatColor.RED + "You cannot teleport because the island you are teleporting to is locked.");
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getEntity().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(damager.getUniqueId()))
            return;

        event.setDamage(0D);
        damager.sendMessage(ChatColor.RED + "You cannot attack entities on this island.");
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageByEntityEvent event) {
         if (!(event.getEntity() instanceof Player))
             return;

         if (!(event.getDamager() instanceof Player))
             return;

        Player damager = (Player) event.getDamager();
        if (damager == null)
            return;

        Player damaged = (Player) event.getEntity();

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(damaged);

        if (island == null)
            return;

        if (!island.isAllowed(damager.getUniqueId()))
            return;

        event.setDamage(0);
        event.setCancelled(true);
        damager.sendMessage(ChatColor.RED + String.format("Warning: %s is in your island.", damaged.getName()));
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

        Island conflict = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
        if (conflict == null)
            return;

        if (conflict.isAllowed(placer.getUniqueId()))
            return;


        // System.out.println(String.format("conflicting w/ %s & !member", conflict.getName()));

        if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES) && (conflict.getIslandLevel() < 5)) {
            // Can raid
           // System.out.println("can raid");
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

            if (!(block.getType() == Material.TRAPPED_CHEST)) {
                return;
            }

            if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
                return;
            }

            if (SkyBlock.getPlugin().getIslandRegistry().conflicts(block.getLocation())) {

                Island conflict = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(block.getLocation());
                if (conflict.isAllowed(player.getUniqueId()))
                    return;

                if (player.hasPermission("skyblock.bypass"))
                    return;


                player.sendMessage(ChatColor.RED + "You do not have permission to open containers here!");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
            }
        }
    }
}