package nlerik.huntervsrunner;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {
    private final GameManager gameManager;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Logic to handle player movement event
        //if player is runner update compasses in inventories of hunters

        if (!gameManager.IsGameRunning()) { return; }

        if (event.getPlayer().equals(gameManager.getRunner())) {
            for (Player hunter : gameManager.getHunters()) {
                hunter.setCompassTarget(event.getPlayer().getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Logic to handle player death event
        //if player is hunter give a new compass
        if (!gameManager.IsGameRunning()) { return; }

        if (gameManager.getHunters().contains(event.getPlayer())) {
            //add compass to inventory
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }
}
