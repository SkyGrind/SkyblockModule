package net.skygrind.skyblock.command.island;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

import java.util.UUID;

/**
 * Created by Matt on 2017-02-25.
 */
public class Leave extends RDCommand {

    IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    protected Leave() {
        super("leave");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/island leave");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have an island to leave!");
            return;
        }

        if (island.getOwner().equals(player.getUniqueId())) {
            if (island.getMembers().size() > 0) {
                MessageUtil.sendGood(player, "Opening owner selection...");
                openNewOwnerSelector(player, island);
                return;
            } else {
                registry.deleteIsland(player, island);
                Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + island.getName() + " Status: " + ChatColor.RED + ChatColor.BOLD.toString() + "[FALLEN]");
            }
        } else {

            if (island.getMembers().contains(player.getUniqueId())) {
                island.getMembers().remove(player.getUniqueId());
            }
        }
        player.teleport(SkyBlock.getPlugin().getSpawn());
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Successfully left your island!");
        return;
    }

    public void openNewOwnerSelector(Player player, Island island) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Owner Selection");
        for (UUID uuid : island.getMembers()) {

            Player pl = Bukkit.getPlayer(uuid);
            if (pl == null || !pl.isOnline()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                meta.setOwner(offlinePlayer.getName());
                meta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + offlinePlayer.getName());
                is.setItemMeta(meta);
                inventory.addItem(is);
            }
            else {
                ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                meta.setOwner(player.getName());
                meta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + pl.getName());
                is.setItemMeta(meta);
                inventory.addItem(is);
            }
        }
        player.openInventory(inventory);
    }
}
