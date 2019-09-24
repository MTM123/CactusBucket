package lv.mtm123.cactusbucket.listener;

import de.tr7zw.itemnbtapi.NBTItem;
import lv.mtm123.cactusbucket.Bucket;
import lv.mtm123.cactusbucket.CactusBucket;
import lv.mtm123.cactusbucket.hooks.BuildHookManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final CactusBucket plugin;
    private final BuildHookManager buildHookManager;
    private final long period;

    public PlayerListener(CactusBucket plugin, BuildHookManager buildHookManager, long period) {
        this.buildHookManager = buildHookManager;
        this.plugin = plugin;
        this.period = period;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getItem() == null || event.getMaterial() == Material.AIR)
            return;

        ItemStack item = event.getItem();
        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey(CactusBucket.getBucketId()))
            return;

        event.setCancelled(true);

        Block relative = event.getClickedBlock().getRelative(event.getBlockFace());

        BlockBreakEvent bevent = new BlockBreakEvent(relative, event.getPlayer());
        plugin.getServer().getPluginManager().callEvent(bevent);

        if (bevent.isCancelled()) {
            return;
        }

        if (item.getAmount() == 1) {
            event.getPlayer().setItemInHand(null);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        new Bucket(buildHookManager, event.getPlayer(), relative).runTaskTimer(plugin, 0, period);

    }

}
