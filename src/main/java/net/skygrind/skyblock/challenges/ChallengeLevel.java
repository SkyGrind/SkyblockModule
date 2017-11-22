package net.skygrind.skyblock.challenges;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Matt on 23/08/2017.
 */

public class ChallengeLevel {

    private final String name;
    private final ItemStack display;

    //Required level to use challenges under this level
    private final String requiredLevel;
    private final String unlockMessage;

    private final List<ItemStack> rewardItems;

    private final int rewardMoney;
    private final int rewardXP;

    private final List<String> permRewards;
    private final List<String> commands;

    public ChallengeLevel(String name, ItemStack display, String requiredLevel, String unlockMessage, List<ItemStack> rewardItems, int rewardMoney, int rewardXP, List<String> permRewards, List<String> commands) {
        this.name = name;
        this.display = display;
        this.requiredLevel = requiredLevel;
        this.unlockMessage = unlockMessage;
        this.rewardItems = rewardItems;
        this.rewardMoney = rewardMoney;
        this.rewardXP = rewardXP;
        this.permRewards = permRewards;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public String getUnlockMessage() {
        return unlockMessage;
    }

    public String getRequiredLevel() {
        return requiredLevel;
    }

    public List<ItemStack> getRewardItems() {
        return rewardItems;
    }

    public int getRewardMoney() {
        return rewardMoney;
    }

    public int getRewardXP() {
        return rewardXP;
    }

    public List<String> getPermRewards() {
        return permRewards;
    }

    public List<String> getCommands() {
        return commands;
    }
}
