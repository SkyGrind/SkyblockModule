package net.skygrind.skyblock.shop;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Matt on 18/03/2017.
 */

public class ShopItem {
    private final String name;
    private final int price;
    private final ItemStack item;
    private final int amount;
    private final UUID category;

    public ShopItem(String name, int price, ItemStack itemStack, int amount, UUID category) {
        this.name = name;
        this.item = itemStack;
        this.price = price;
        this.amount = amount;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public UUID getCategory() {
        return category;
    }
}
