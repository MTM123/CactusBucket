package lv.mtm123.cactusbucket;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import de.tr7zw.itemnbtapi.NBTItem;
import lv.mtm123.cactusbucket.command.CPlayer;
import lv.mtm123.cactusbucket.command.CommandMain;
import lv.mtm123.cactusbucket.hooks.BuildHookManager;
import lv.mtm123.cactusbucket.listener.PlayerListener;
import lv.mtm123.spigotutils.ConfigManager;
import lv.mtm123.spigotutils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CactusBucket extends JavaPlugin {

    private static final String BUCKET_ID = "fallbucket";

    private CactusBucket plugin;
    private ItemStack cactusBucketItem;

    public static String getBucketId() {
        return BUCKET_ID;
    }

    @Override
    public void onEnable() {
        plugin = this;

        ConfigManager.initialize(plugin);

        load();
        registerCommands();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(plugin);
        plugin = null;
    }

    public ItemStack getCactusBucketItem() {
        return cactusBucketItem;
    }

    private void load() {

        FileConfiguration cfg = ConfigManager.load("config.yml");

        loadCactusBucketItem(cfg);

        BuildHookManager buildHookManager = new BuildHookManager();

        buildHookManager.addBuildHook(((player, block) -> {
            BlockBreakEvent event = new BlockBreakEvent(block, player);
            Bukkit.getPluginManager().callEvent(event);

            return !event.isCancelled();
        }));

        buildHookManager.addBuildHook(((player, block) -> block.getType() == Material.AIR));

        long period = cfg.getLong("bucket-speed-in-ticks");
        period = period > 0 ? period : 1;

        getServer().getPluginManager().registerEvents(new PlayerListener(plugin, buildHookManager, period), plugin);

    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        load();
    }

    private void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.getCommandContexts().registerContext(CPlayer.class, c -> {

            Player target = Bukkit.getPlayer(c.popFirstArg());

            if (target == null) {

                if ((c.getSender() instanceof ConsoleCommandSender)
                        || (c.getSender() instanceof RemoteConsoleCommandSender)) {
                    throw new InvalidCommandArgument("You must provide a valid target player!");
                } else {
                    return new CPlayer((Player) c.getSender());
                }

            } else {
                return new CPlayer(target);
            }

        });

        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new CommandMain());
    }

    private void loadCactusBucketItem(FileConfiguration cfg) {
        ItemBuilder ib = new ItemBuilder();

        String[] idata = cfg.getString("bucket-item.id").split(":");

        Material mat = Material.matchMaterial(idata[0]);
        if (mat != null) {
            ib.withMat(mat);
        }

        if (idata.length == 2) {
            short dur = Short.parseShort(idata[1]);
            ib.withData(dur);
        }

        ib.withName(cfg.getString("bucket-item.name"));

        boolean glow = cfg.getBoolean("bucket-item.glow");
        if (glow) {
            ib.addUnsafeEnchant(Enchantment.ARROW_INFINITE, 1);
            ib.addFlags(ItemFlag.HIDE_ENCHANTS);
        }

        ib.withLore(cfg.getStringList("bucket-item.lore"));

        ItemStack i = ib.build();

        NBTItem nbtItem = new NBTItem(i);
        nbtItem.setBoolean(BUCKET_ID, true);

        cactusBucketItem = nbtItem.getItem();
    }

}
