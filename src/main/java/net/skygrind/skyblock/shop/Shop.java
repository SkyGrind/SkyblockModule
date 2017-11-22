package net.skygrind.skyblock.shop;

import lombok.Getter;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.ItemUtils;
import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.player.SkyPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Matt on 18/03/2017.
 */
public class Shop {

    @Getter private String name;
    private List<ShopCategory> categories;

    public Shop(final String name)
    {
        this.name = name;
        this.categories = Collections.synchronizedList(new ArrayList<>());
    }

    public Shop(String name, ShopCategory... categories) {
        this.name = name;
        this.categories = Arrays.asList(categories);
    }

    public void addCategory(ShopCategory category)
    {
        this.categories.add(category);
    }

    public ShopCategory getCategory(InventoryClickEvent event) {
        for (ShopCategory category : categories) {
            if (category.getName().equalsIgnoreCase(event.getInventory().getName())) {
                return category;
            }
        }
        return null;
    }

    public ShopCategory getCategory(UUID uuid)
    {
        return this.categories.stream().filter(c -> c.getShopId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void buy(SkyPlayer player, InventoryClickEvent event) {
        ShopCategory category = getCategory(event);

        if (category == null) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        ShopItem shopItem = category.getItem(item);

        if (shopItem == null) {
            return;
        }

        if (!player.purchase(shopItem.getPrice())) {
            player.getBukkitPlayer().closeInventory();
            MessageUtil.sendUrgent(player.getBukkitPlayer(), "Insufficient funds!");
            return;
        }

        player.getBukkitPlayer().getInventory().addItem(new ItemStack(shopItem.getItem().getType(), shopItem.getAmount()));
        MessageUtil.sendInfo(player.getBukkitPlayer(), "Purchased: " + shopItem.getAmount() + " x " + shopItem.getName());
    }

    public void sell(SkyPlayer player, ShopItem item) {

        List<ItemStack> toSell = Collections.synchronizedList(new ArrayList<>());

        for(ItemStack itemStack : player.getBukkitPlayer().getInventory())
            if(itemStack.getType().equals(item.getItem().getType()))
                toSell.add(itemStack);

        int amount = item.getPrice() * toSell.size();

        if(!player.sell(amount)) {
            MessageUtil.sendUrgent(player.getBukkitPlayer(), "Max balance reached!");
            return;
        }

        toSell.forEach(itemStack -> player.getBukkitPlayer().getInventory().remove(itemStack));

        MessageUtil.sendInfo(player.getBukkitPlayer(), "Sold: " + item.getAmount() + " x " + item.getName());
    }
}
