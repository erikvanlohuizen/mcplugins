package nlerik.huntervsrunner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

    private Map<World.Environment, Location> runnerLocations = new HashMap<>();

    public boolean IsGameRunning() {
        return gameRunning;
    }

    public void SetGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void StopGame() {
        gameRunning = false;
    }

    public void updateRunnerLocation(World.Environment dimension, Location location) {
        runnerLocations.put(dimension, location);
    }

    public Location getLastLocation(World.Environment dimension) {

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

            if (!this.IsGameRunning()) {
                return;
            }

            World.Environment runnerDimension = runner.getWorld().getEnvironment();
            this.updateRunnerLocation(runnerDimension, runner.getLocation());

            for (Player hunter : this.getHunters()) {

                World.Environment hunterDimension = hunter.getWorld().getEnvironment();

                if ((runnerDimension == World.Environment.NORMAL || runnerDimension == World.Environment.NETHER) && hunterDimension == World.Environment.NORMAL) {
                    hunter.setCompassTarget(this.getLastLocation(hunterDimension));
                }

                if (runnerDimension == World.Environment.NETHER && hunterDimension == World.Environment.NETHER) {
                    updateCompassInInventory(hunter, runner.getLocation());
                }

                if (runnerDimension == World.Environment.THE_END && hunterDimension == World.Environment.THE_END) {
                    updateCompassInInventory(hunter, runner.getLocation());
                }
            }
        }, delay, delay);
    }

    public void updateCompassInInventory(Player hunter, Location targetLocation) {
        // Iterate over all items in the player's inventory
        for (ItemStack item : hunter.getInventory().getContents()) {
            // Check if the item is not null and is a compass
            if (item != null && item.getType() == Material.COMPASS) {
                // Cast the item's meta to CompassMeta
                CompassMeta compassMeta = (CompassMeta) item.getItemMeta();

                if (compassMeta != null) {
                    // Here you can modify the compassMeta, for example:
                    compassMeta.setLodestone(targetLocation);
                    compassMeta.setLodestoneTracked(false); // Set to false to avoid the compass being linked to a lodestone block

                    // Apply the modified meta back to the item
                    item.setItemMeta(compassMeta);

                    // If you only want to update the first compass found, break the loop
                    break;
                }
            }
        }
    }
}
