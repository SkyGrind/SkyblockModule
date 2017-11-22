package net.skygrind.skyblock.command.shop;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.shop.Shop;
import net.skygrind.skyblock.shop.ShopCategory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

import java.util.UUID;

public class ShopAddCategoryCommand extends RDCommand
{
    public ShopAddCategoryCommand()
    {
        super("addcategory");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException
    {
        if (args.length != 2) {
            MessageUtil.sendUrgent(player, "/shop createCategory <ShopName> <CategoryName>");
            return;
        }

        String name = args[0];
        Shop shop = SkyBlock.getPlugin().getShopHandler().findByName(name);
        if(shop == null)
        {
            MessageUtil.sendUrgent(player, "A shop with the name '" + args[0] + "' does not exist.");
            return;
        }
        String category = args[1];
        ShopCategory category1 = new ShopCategory(UUID.randomUUID(), category, player.getItemInHand());
        shop.addCategory(category1);
        MessageUtil.sendGood(player, "You have created the category '" + category + "' within the shop '" + shop + "'.");
    }
}
