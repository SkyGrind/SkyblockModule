package net.skygrind.skyblock.island.listeners;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import sun.applet.Main;


public class GeneralListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) {
            int joinNumber = SkyBlock.getPlugin().getServerConfig().getPlayersJoined();
            event.getPlayer().teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            Bukkit.broadcastMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Sky" + ChatColor.WHITE + ChatColor.BOLD + "Paradise " + ChatColor.GRAY + "\u00BB " + ChatColor.YELLOW +
                    String.format("Welcome %s to the ", event.getPlayer().getName()) + ChatColor.AQUA + ChatColor.BOLD.toString() + "Sky" + ChatColor.WHITE + ChatColor.BOLD + "Paradise" + ChatColor.YELLOW + String.format(" (%s)", joinNumber));
            SkyBlock.getPlugin().getServerConfig().setPlayersJoined(joinNumber + 1);
        }

        event.setJoinMessage(null);
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

        if (player.hasPermission("skygrind.keepinv")) {
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

        MessageUtil.sendUrgent(damager, "You cannot do this in spawn!");

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
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!(player.getWorld() == SkyBlock.getPlugin().getServerConfig().getSpawnLocation().getWorld()))
            return;

        if(event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            return;
        }

        event.setCancelled(true);
    }
}

