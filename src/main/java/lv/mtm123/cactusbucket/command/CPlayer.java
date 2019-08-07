package lv.mtm123.cactusbucket.command;

import org.bukkit.entity.Player;

public class CPlayer {

    private final Player player;

    public CPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
