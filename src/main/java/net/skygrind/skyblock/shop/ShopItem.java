package net.skygrind.skyblock.shop;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Matt on 18/03/2017.
 */

@RequiredArgsConstructor
@Data
public class ShopItem {

    private final String name;
    private final int price;
    private final ItemStack item;
    private final int amount;
    private final UUID category;
}
