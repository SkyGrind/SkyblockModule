package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
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

import java.util.UUID;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandLeaveCommand extends GooseCommand {

    private final IslandRegistry registry;

    public IslandLeaveCommand() {
        super("leave", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island kick");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        if (island.getOwner().equals(player.getUniqueId())) {
            if (island.getMembers().size() > 0) {
                MessageUtil.sendGood(player, "Opening owner selection...");
                openNewOwnerSelector(player, island);
                return;
            } else {
                registry.deleteIsland(player, island);

//                Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + island.getName() + " Status: " + ChatColor.RED + ChatColor.BOLD.toString() + "[FALLEN]");
            }
        } else {

            if (island.getMembers().contains(player.getUniqueId())) {
                island.getMembers().remove(player.getUniqueId());
            }
        }
        player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        MessageUtil.sendServerTheme(player, ChatColor.GREEN + "You have successfully left your island.");
        return;
    }

    private void openNewOwnerSelector(Player player, Island island) {
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
            } else {
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
