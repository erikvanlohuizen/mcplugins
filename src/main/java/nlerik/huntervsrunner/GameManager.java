package nlerik.huntervsrunner;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Player runner;
    private List<Player> hunters = new ArrayList<>();

    public Player getRunner() {
        return runner;
    }

    public List<Player> getHunters() {
        return hunters;
    }
}
