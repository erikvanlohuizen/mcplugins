package nlerik.huntervsrunner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private boolean gameRunning = false;
    private Player runner;
    private List<Player> hunters = new ArrayList<>();

    private Map<Integer, Location> runnerLocations = new HashMap<>();

    public boolean IsGameRunning() {
        return gameRunning;
    }

    public void SetGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void StopGame() {
        gameRunning = false;
    }

    public void updateRunnerLocation(int dimension, Location location) {
        runnerLocations.put(dimension, location);
    }

    public Location getLastLocation(int dimension) {

        if (!runnerLocations.containsKey(dimension)) {
            return null;
        }

        return runnerLocations.get(dimension);
    }

    public void AddHunter(Player player) {
        //only add when game is not running and if not already in list
        if (gameRunning || hunters.contains(player)) {
            return;
        }

        hunters.add(player);
    }

    public void SetRunner(Player player) {

        if (gameRunning) {
            return;
        }

        runner = player;
    }

    public void ClearTeams() {
        hunters.clear();
        runner = null;
    }

    public Player getRunner() {
        return runner;
    }

    public List<Player> getHunters() {
        return hunters;
    }

    public void startCompassUpdater(JavaPlugin pluginInstance) {
        int delay = 20 * 1; // 1 second
        Bukkit.getScheduler().runTaskTimer(pluginInstance, () -> {

            if (!this.IsGameRunning()) { return; }

            int runnerDimension = runner.getWorld().getEnvironment().getId();
            this.updateRunnerLocation(runnerDimension, runner.getLocation());

            for (Player hunter : this.getHunters()) {

                int hunterDimension = hunter.getWorld().getEnvironment().getId();
                Location runnerLocation = this.getLastLocation(hunterDimension);

                if (runnerLocation == null || hunterDimension != runnerDimension) {

                    //get compass itemstack from inventory, not from a specified slot like mainhand
                    ItemStack held = hunter.getInventory().getItemInMainHand();
                    if (held.getType() != Material.COMPASS) {
                        continue;
                    }
                    final CompassMeta meta = (CompassMeta) held.getItemMeta(); // We know that the item in this even is a compass because of our check, so we the item's ItemMeta to CompassMeta.
                    // Should be noted CompassMeta does extend ItemMeta.
                    meta.setLodestone(runner.getLocation()); // Using a second account to test this, called the accounts name directly. You'll probably want to implement some null checking.
                    meta.setLodestoneTracked(false); // If this is set to true (default) it will not work as it would require a lodestone to be present at the location. Just set it to false.
                    meta.setDisplayName("This is a tracker."); // Set a display name because hey, I want to be fancy.
                    held.setItemMeta(meta); // Update the item's ItemMeta to our new updated one.


                } else {
                    hunter.setCompassTarget(runnerLocation);
                }
            }
        }, delay, delay);
    }
}
