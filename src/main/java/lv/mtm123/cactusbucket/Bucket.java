package lv.mtm123.cactusbucket;

import lv.mtm123.cactusbucket.hooks.BuildHookManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Bucket extends BukkitRunnable {

    private final BuildHookManager buildHookManager;

    private final Player player;
    private final Material[] sequence = new Material[]{Material.TRIPWIRE, Material.SAND, Material.CACTUS, Material.AIR};
    private Block startBlock;
    private int currentPosition;

    public Bucket(BuildHookManager buildHookManager, Player player, Block startBlock) {
        this.buildHookManager = buildHookManager;
        this.player = player;
        this.startBlock = startBlock;
        currentPosition = 1;
    }

    @Override
    public void run() {

        if (!buildHookManager.canBuild(player, startBlock)) {
            cancel();
            return;
        }

        Material mat = sequence[currentPosition];
        startBlock.setType(mat);

        if (currentPosition == sequence.length - 1) {
            currentPosition = 0;
        } else {
            currentPosition++;
        }

        startBlock = startBlock.getRelative(BlockFace.UP);

    }

}
