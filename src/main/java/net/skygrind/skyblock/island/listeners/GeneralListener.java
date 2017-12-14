package net.skygrind.skyblock.island.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.MessageUtil;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;


public class GeneralListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + " Welcome " + ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " to " + ChatColor.AQUA + ChatColor.BOLD.toString() + "Sky" + ChatColor.WHITE + ChatColor.BOLD + "Paradise" + ChatColor.YELLOW + "!");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + " Website:" + ChatColor.WHITE + " http://skyparadise-mc.com");
        player.sendMessage(ChatColor.YELLOW + " Discord:" + ChatColor.WHITE + " https://discord.gg/q8GZhgR");
        player.sendMessage(ChatColor.YELLOW + " Shop:" + ChatColor.WHITE + " http://shop.skyparadisemc.com");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

        if (!player.hasPlayedBefore()) {
            SkyBlock.getPlugin().getServerConfig().incrementPlayersJoined();
            int joinNumber = SkyBlock.getPlugin().getServerConfig().getPlayersJoined();
            Bukkit.broadcastMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Sky" + ChatColor.WHITE + ChatColor.BOLD + "Paradise " + ChatColor.GRAY + "\u00BB " + ChatColor.YELLOW + String.format("Welcome %s to the ", player.getName()) + ChatColor.AQUA + ChatColor.BOLD.toString() + "Sky" + ChatColor.WHITE + ChatColor.BOLD + "Paradise" + ChatColor.YELLOW + String.format(" (%s)", joinNumber));
            player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location spawn = SkyBlock.getPlugin().getServerConfig().getSpawnLocation();
        player.teleport(spawn);
        event.setRespawnLocation(spawn);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.hasPermission("islesmc.keepinv")) {
            event.setKeepInventory(true);
        } else {
            event.setKeepInventory(false);
        }
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!(damaged.getWorld() == SkyBlock.getPlugin().getServerConfig().getSpawnLocation().getWorld()))
            return;

        MessageUtil.sendServerTheme(damager, "You cannot damage other players in spawn.");

        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) return;

        Player player = (Player) event.getEntity();

        if (!(player.getWorld() == SkyBlock.getPlugin().getServerConfig().getSpawnLocation().getWorld())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onGiftPlace(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }

        ItemStack item = event.getPlayer().getItemInHand();

        if (item.getType() != Material.SKULL && item.getType() != Material.SKULL_ITEM) {
            return;
        }

        if (item.getItemMeta() == null) {
            return;
        }

        if (item.getItemMeta().getDisplayName() == null) {
            return;
        }

        if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Christmas Present")) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        ItemStack[] items = new ItemStack[]{new ItemStack(Material.CHEST, 1), new ItemStack(Material.ICE, 2), new ItemStack(Material.MELON, 1),
                new ItemStack(Material.BONE, 1), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.MELON_SEEDS, 1),
                new ItemStack(Material.SUGAR_CANE, 1), new ItemStack(Material.RED_MUSHROOM, 1), new ItemStack(Material.BROWN_MUSHROOM, 1),
                new ItemStack(Material.CACTUS, 1), new ItemStack(Material.PUMPKIN_SEEDS, 1)};

        event.getPlayer().getInventory().removeItem(item);
        event.getPlayer().getInventory().addItem(items);

        MessageUtil.sendServerTheme(event.getPlayer(), ChatColor.GREEN + "You have opened your Christmas Present!");
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!(player.getWorld() == SkyBlock.getPlugin().getServerConfig().getSpawnLocation().getWorld()))
            return;

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            return;
        }

        event.setCancelled(true);
    }
}

