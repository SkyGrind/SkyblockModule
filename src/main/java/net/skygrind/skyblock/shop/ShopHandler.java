package net.skygrind.skyblock.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopHandler {
    private final List<Shop> shops;

    public ShopHandler() {
        this.shops = Collections.synchronizedList(new ArrayList<>());
    }

    public void addShop(Shop shop) {
        this.shops.add(shop);
    }

    public Shop findByName(String name) {
        return this.shops.stream().filter(shop -> shop.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
