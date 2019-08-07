package lv.mtm123.cactusbucket.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface BuildCheck {

    boolean canBuild(Player player, Block block);

}
