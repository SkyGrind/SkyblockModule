package net.skygrind.skyblock.shop;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Matt on 18/03/2017.
 */
public class Shop {
    private static final int MAX_BALANCE = 1000000000;

    private String name;
    private List<ShopCategory> categories;

    public Shop(final String name) {
        this.name = name;
        this.categories = Collections.synchronizedList(new ArrayList<>());
    }

    public Shop(String name, ShopCategory... categories) {
        this.name = name;
        this.categories = Arrays.asList(categories);
    }

    public void addCategory(ShopCategory category) {
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

    public ShopCategory getCategory(UUID uuid) {
        return this.categories.stream().filter(c -> c.getShopId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void buy(Player player, InventoryClickEvent event) {
        ShopCategory category = getCategory(event);

        if (category == null) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        ShopItem shopItem = category.getItem(item);

        if (shopItem == null) {
            return;
        }

        if (!purchase(player, shopItem.getPrice())) {
            player.closeInventory();
            MessageUtil.sendUrgent(player, "Insufficient funds!");
            return;
        }

        player.getInventory().addItem(new ItemStack(shopItem.getItem().getType(), shopItem.getAmount()));
        MessageUtil.sendInfo(player, "Purchased: " + shopItem.getAmount() + " x " + shopItem.getName());
    }

    public void sell(Player player, ShopItem item) {

        List<ItemStack> toSell = Collections.synchronizedList(new ArrayList<>());

        for (ItemStack itemStack : player.getInventory())
            if (itemStack.getType().equals(item.getItem().getType()))
                toSell.add(itemStack);

        int amount = item.getPrice() * toSell.size();

        if (!sell(player, amount)) {
            MessageUtil.sendUrgent(player, "Max balance reached!");
            return;
        }

        toSell.forEach(itemStack -> player.getInventory().remove(itemStack));

        MessageUtil.sendInfo(player, "Sold: " + item.getAmount() + " x " + item.getName());
    }

    private boolean sell(final Player player, final int price) {
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("skyblock");
        Integer balance = profile.getDouble("balance").intValue();
        if ((balance + price) < MAX_BALANCE) {
            balance += price;
            profile.set("balance", balance.doubleValue());
            return true;
        }
        return false;
    }

    private boolean purchase(final Player player, final int price) {
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("skyblock");
        Integer balance = profile.getDouble("balance").intValue();
        if (balance - price > 0) {
            balance = (balance - price);
            profile.set("balance", balance.doubleValue());
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }
}
