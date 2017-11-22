package net.skygrind.skyblock.shop;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import sun.plugin.util.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matt on 18/03/2017.
 */
public class ShopCategory {

    @Getter private final String name;
    @Getter private final ItemStack icon;
    @Getter private final List<ShopItem> items;
    @Getter private final UUID shopId;

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
}
