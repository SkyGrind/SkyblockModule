package net.skygrind.skyblock.player;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.Getter;
import net.skygrind.core.Core;
import net.skygrind.core.data.SkyData;
import net.skygrind.skyblock.SkyBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 2017-02-11.
 */
public class SkyPlayer {

    private final int maxBalance = 1000000000;

    @Getter
    private Player bukkitPlayer;
    private DBObject dbObject;

    @Getter
    private int balance;

    @Getter
    private int moneySpent;

    public SkyPlayer(Player bukkitPlayer, DBObject object) {
        this.bukkitPlayer = bukkitPlayer;
        this.dbObject = object;
    }
    
    public boolean purchase(int price) {
        if (balance - price > 0) {
            this.balance = (balance - price);
            return true;
        } else {
            return false;
        }
    }

    public boolean sell(int price) {
        if ((balance + price) < maxBalance) {
            balance += price;
            return true;
        }
        return false;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }
}
