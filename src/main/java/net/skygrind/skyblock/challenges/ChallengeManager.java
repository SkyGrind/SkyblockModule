package net.skygrind.skyblock.challenges;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Created by Matt on 23/08/2017.
 */
public class ChallengeManager {

    private SkyBlock plugin;

    private Map<ChallengeLevel, List<Challenge>> challenges = new HashMap<>();

    public ChallengeManager(SkyBlock plugin) {
        this.plugin = plugin;
    }

    public void registerLevels() {
        challenges.clear();

        for (String levels : plugin.getChallengesConfig().getConfigurationSection("challenges.levelUnlock").getKeys(false)) {

            String base = plugin.getChallengesConfig().getString("challenges.levelUnlock." + levels);

            /**
             * Constructor arguments
             */

            String name = plugin.getChallengesConfig().getString(base + ".name");

            ItemStack item = new ItemStack(Material.getMaterial(plugin.getChallengesConfig().getString(base + ".display").split(" ")[0]),
                    Integer.valueOf(plugin.getChallengesConfig().getString(base + ".display").split(" ")[1]));

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);

            String requiredLevel = plugin.getChallengesConfig().getString(base + ".required");
            String unlockMessage = plugin.getChallengesConfig().getString(base + ".message");

            List<ItemStack> rewardItems = new ArrayList<>();

            for (String s : plugin.getChallengesConfig().getString(base + ".rewardItems").split(" ")) {

                ItemStack i = new ItemStack(Material.getMaterial(s.split(":")[0]), Integer.valueOf(s.split(":")[1]), s.split(":")[2] == null ? (short) 0 : (short) Short.valueOf(s.split(":")[2]));
                rewardItems.add(i);
            }

            int rewardMoney = plugin.getChallengesConfig().getInt(base + ".moneyReward");
            int xpReward = plugin.getChallengesConfig().getInt(base + ".expReward");
            List<String> permissionReward = Arrays.asList(plugin.getChallengesConfig().getString(base + ".permReward").split(" "));
            List<String> commands = Arrays.asList(plugin.getChallengesConfig().getString(base + ".commands"));

            ChallengeLevel level = new ChallengeLevel(name, item, requiredLevel, unlockMessage, rewardItems, rewardMoney, xpReward, permissionReward, commands);
            this.challenges.put(level, new ArrayList<>());
        }
        registerChalleneges();
    }

    public void registerChalleneges() {
        for (String level : plugin.getChallengesConfig().getConfigurationSection("challengeList").getKeys(false)) {

            for (String challenge : plugin.getChallengesConfig().getConfigurationSection("challengeList." + level).getKeys(false)) {

                String base = plugin.getChallengesConfig().getString("challengeList." + level + "." + challenge);

                /**
                 * Constructor arguments
                 */

                String name = plugin.getChallengesConfig().getString(base + ".name");
                ItemStack display = new ItemStack(Material.getMaterial(plugin.getChallengesConfig().getString(base + ".display").split(":")[0]), 1);
                String desc = plugin.getChallengesConfig().getString(base + ".desc");

                ItemMeta meta = display.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', desc)));
                display.setItemMeta(meta);

                String type = plugin.getChallengesConfig().getString(base + ".type");
                ChallengeType cType = ChallengeType.valueOf(type.toUpperCase());

                List<String> requiredItems = Arrays.asList(plugin.getChallengesConfig().getString(base + ".requiredItems").split(" "));

                List<String> serItems = Arrays.asList(plugin.getChallengesConfig().getString(base + ".rewardItems").split(" "));

                List<String> rewardCommands = plugin.getChallengesConfig().getStringList(base + ".rewardCommands");

                List<ItemStack> rewards = new ArrayList<>();

                for (String item : serItems) {

                    ItemStack itemStack = null;

                    String[] split = item.split(":");
                    if (split.length == 3) {
                        itemStack = new ItemStack(Material.getMaterial(split[0]), Integer.valueOf(split[1]), Short.valueOf(split[3]));
                    } else if (split.length == 2) {
                        itemStack = new ItemStack(Material.getMaterial(split[0]), Integer.valueOf(split[1]));
                    }

                    rewards.add(itemStack);
                }

                boolean takeItems = plugin.getChallengesConfig().getBoolean("takeItems");

                ChallengeLevel lev = byName(level);

                Challenge chal = new Challenge(name, display, desc, cType, requiredItems, lev, rewards, rewardCommands, takeItems);
                challenges.get(lev).add(chal);
            }
        }
    }

    public ChallengeLevel byName(String name) {
        for (ChallengeLevel lev : challenges.keySet()) {
            if (lev.getName().equalsIgnoreCase(name)) {
                return lev;
            }
        }
        return null;
    }

    public void unlockedChallenges() {

    }
}
