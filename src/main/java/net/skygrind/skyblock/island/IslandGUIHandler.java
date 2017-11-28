package net.skygrind.skyblock.island;

import com.islesmc.modules.api.API;
import com.sk89q.worldedit.MaxChangedBlocksException;
import net.md_5.bungee.api.ChatColor;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandGUIHandler implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player pl = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if (event.getClickedInventory().getName().equalsIgnoreCase("Island Selection")) {


            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();

            IslandType type = IslandType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName().replace(" ", "_")).toUpperCase());

            pl.closeInventory();

            try {
                SkyBlock.getPlugin().getIslandRegistry().createIsland(pl, type);
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        } else if (event.getClickedInventory().getName().equalsIgnoreCase("Owner Selection")) {

            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();

            IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

            Island playerIsland = registry.getIslandForPlayer(pl);

            String chosen = org.bukkit.ChatColor.stripColor(meta.getDisplayName());

            Player newOwner = Bukkit.getPlayer(chosen);

            if (newOwner == null || !newOwner.isOnline()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(chosen);
                playerIsland.setOwner(player.getUniqueId());
                registry.playerIslands.get(registry.playerIslands.indexOf(playerIsland)).setOwner(player.getUniqueId());
                playerIsland.getMembers().remove(player.getUniqueId());
                pl.closeInventory();

                File file = new File(API.getPlugin().getDataFolder() + "/skyblock", pl.getUniqueId().toString() + ".yml");
                file.renameTo(new File(API.getPlugin().getDataFolder() + "/skyblock", player.getUniqueId().toString() + ".yml"));

                MessageUtil.sendGood(pl, "Set your island's owner to: " + org.bukkit.ChatColor.GOLD + player.getName());
            } else {
                playerIsland.setOwner(newOwner.getUniqueId());
                registry.playerIslands.get(registry.playerIslands.indexOf(playerIsland)).setOwner(newOwner.getUniqueId());
                playerIsland.getMembers().remove(newOwner.getUniqueId());
                pl.closeInventory();
                MessageUtil.sendGood(pl, "Set your island's owner to: " + org.bukkit.ChatColor.GOLD + newOwner.getName());
                MessageUtil.sendGood(newOwner, "You have been promoted to owner status on " + org.bukkit.ChatColor.GOLD + playerIsland.getName() + "'s " + ChatColor.GREEN + " island");

                File file = new File((API.getPlugin()).getDataFolder() + "/skyblock", pl.getUniqueId().toString() + ".yml");
                file.renameTo(new File(API.getPlugin().getDataFolder() + "/skyblock", newOwner.getUniqueId().toString() + ".yml"));
            }
            pl.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            pl.sendMessage(org.bukkit.ChatColor.GREEN + org.bukkit.ChatColor.BOLD.toString() + "[!] " + org.bukkit.ChatColor.GRAY + "Successfully left your island!");
        }
    }
}
