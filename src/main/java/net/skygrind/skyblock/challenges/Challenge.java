package net.skygrind.skyblock.challenges;

import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Matt on 23/08/2017.
 */
@RequiredArgsConstructor
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
}
