package net.skygrind.skyblock.shop;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matt on 18/03/2017.
 */
public class ShopCategory {
    private final String name;
    private final ItemStack icon;
    private final List<ShopItem> items;
    private final UUID shopId;

    public ShopCategory(UUID shopId, String name, ItemStack icon) {
        this.shopId = shopId;
        this.name = name;
        this.icon = icon;
        this.items = new ArrayList<>();
    }

    public ShopCategory addItem(ShopItem item) {
        this.items.add(item);
        return this;
    }

    public ShopItem getItem(ItemStack item) {
        for (ShopItem item1 : items) {
            if (item1.getItem().getType() == item.getType()) {
                return item1;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public UUID getShopId() {
        return shopId;
    }
}
