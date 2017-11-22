package net.skygrind.skyblock.command.shop;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.shop.Shop;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import sun.applet.Main;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

public class ShopCreateCommand extends RDCommand
{
    public ShopCreateCommand()
    {
        super("create");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException
    {
        if (args.length != 1) {
            MessageUtil.sendUrgent(player, "/shop create <Name>");
            return;
        }

        String name = args[0];
        Shop shop = SkyBlock.getPlugin().getShopHandler().findByName(name);
        if(shop != null)
        {
            MessageUtil.sendUrgent(player, "A shop with the name '" + args[0] + "' already exists.");
            return;
        }
        shop = new Shop(name);
        SkyBlock.getPlugin().getShopHandler().addShop(shop);
        MessageUtil.sendGood(player, "You have created the shop '" + name + "'");
    }
}
