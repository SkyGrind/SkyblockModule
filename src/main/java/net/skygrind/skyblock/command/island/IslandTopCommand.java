package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * Created by Matt on 17/04/2017.
 */
public class IslandTopCommand extends GooseCommand implements Listener {
    private final IslandRegistry registry;
    private Inventory islandTop = Bukkit.createInventory(null, 36, "Top Islands");

    public IslandTopCommand() {
        super("top", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();
        SkyBlock.getPlugin().registerEvent(this);
    }

    private Inventory createIslandTopInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "Top Islands");
        Map<Island, Integer> islands = new HashMap<>();

        for (Island island : registry.getPlayerIslands()) {
            islands.put(island, island.getIslandLevel());
        }

        int topSize = 10;

        if (islands.size() < 10) {
            topSize = islands.size();
        }

        Set<Map.Entry<Island, Integer>> set = islands.entrySet();

        List<Map.Entry<Island, Integer>> list = new ArrayList<>(
                set);

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        for (Map.Entry<Island, Integer> ent : list.subList(0, topSize)) {

            UUID uuid = ent.getKey().getOwner();

            OfflinePlayer player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                player = Bukkit.getOfflinePlayer(uuid);
            }


            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setDisplayName(player.getName() + "'s Island");
            meta.setOwner(player.getName());
            List<String> lore = Lists.newArrayList(String.format("Level: %s", ent.getKey().getIslandLevel()), String.format("Balance: %s", ent.getKey().getBankBalance()));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }
        return inventory;
    }

    @Override
    public void execute(Player player, String[] strings) {
        player.openInventory(createIslandTopInventory());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(event.getInventory().getTitle().equalsIgnoreCase("Top Islands")) {
            event.setResult(Event.Result.DENY);
        }
    }
}
