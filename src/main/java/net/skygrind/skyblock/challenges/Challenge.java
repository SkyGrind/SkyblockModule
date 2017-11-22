package net.skygrind.skyblock.challenges;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Matt on 23/08/2017.
 */
public class Challenge {
    private final String name;
    private final ItemStack display;
    private final String description;
    private final ChallengeType type;
    private final List<String> requiredItems;
    private final ChallengeLevel level;

    private final List<ItemStack> rewardItems;
    private final List<String> rewardCommands;

    private final boolean takeItems;

    public Challenge(String name, ItemStack display, String description, ChallengeType type, List<String> requiredItems, ChallengeLevel level, List<ItemStack> rewardItems, List<String> rewardCommands, boolean takeItems) {
        this.name = name;
        this.display = display;
        this.description = description;
        this.type = type;
        this.requiredItems = requiredItems;
        this.level = level;
        this.rewardItems = rewardItems;
        this.rewardCommands = rewardCommands;
        this.takeItems = takeItems;
    }

    public String getName() {
        return name;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public String getDescription() {
        return description;
    }

    public ChallengeType getType() {
        return type;
    }

    public List<String> getRequiredItems() {
        return requiredItems;
    }

    public ChallengeLevel getLevel() {
        return level;
    }

    public List<ItemStack> getRewardItems() {
        return rewardItems;
    }

    public List<String> getRewardCommands() {
        return rewardCommands;
    }

    public boolean isTakeItems() {
        return takeItems;
    }
}
