package lv.mtm123.cactusbucket.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lv.mtm123.cactusbucket.CactusBucket;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("cactusbucket|cbucket|cb")
@CommandPermission("cactusbucket.commands")
public class CommandMain extends BaseCommand {

    @Dependency
    private CactusBucket plugin;

    @Subcommand("g|give")
    @CommandPermission("cactusbucket.commands.give")
    public void onGive(CommandSender sender, CPlayer target, @Flags("min=1,max=64") @Default("1") int amount) {
        giveCBItemToPlayer(sender, target.getPlayer(), amount);
    }

    @Subcommand("r|reload")
    @CommandPermission("cactusbucket.commands.reload")
    public void onReload(CommandSender sender) {
        plugin.reload();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6CactusBucket&7] &aPlugin reloaded!"));
    }

    @Subcommand("h|help")
    @HelpCommand
    @CommandPermission("cactusbucket.commands.help")
    public void onHelp(CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    private void giveCBItemToPlayer(CommandSender sender, Player target, int amount) {
        ItemStack i = plugin.getCactusBucketItem().clone();
        i.setAmount(amount);

        if (target.getInventory().firstEmpty() != -1) {
            target.getInventory().addItem(i);
        } else {
            target.getWorld().dropItem(target.getLocation(), i);
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6CactusBucket&7] &aItem added!"));
    }

}
