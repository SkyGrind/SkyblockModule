package net.skygrind.skyblock.challenges;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Matt on 23/08/2017.
 */

@Data
@RequiredArgsConstructor
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
}
