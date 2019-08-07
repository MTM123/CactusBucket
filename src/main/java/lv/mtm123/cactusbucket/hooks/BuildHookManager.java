package lv.mtm123.cactusbucket.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BuildHookManager {

    private final Set<BuildCheck> buildHooks;

    public BuildHookManager() {
        this.buildHooks = new HashSet<>();
    }

    public void addBuildHook(BuildCheck buildCheck){
        buildHooks.add(buildCheck);
    }

    public boolean canBuild(Player player, Block block){
        return buildHooks.stream().allMatch(a -> a.canBuild(player, block));
    }

}
