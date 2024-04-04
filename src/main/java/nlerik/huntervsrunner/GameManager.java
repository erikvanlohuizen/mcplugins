package nlerik.huntervsrunner;

import org.bukkit.*;
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

    public void stopGame(boolean runnerWon) {

        // Stop the game & clear teams
        gameRunning = false;
        clearTeams();

        // Send a message to all players about who won the game
        if (runnerWon) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GREEN + "The runner has won the game!");
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + "The hunters have won the game!");
            }
        }
    }

    public void updateRunnerLocation(World.Environment dimension, Location location) {

        // Update the last known location of the runner in the specified dimension
        runnerLocations.put(dimension, location);
    }

    public Location getLastLocation(World.Environment dimension) {

        // Return the last known location of the runner in the specified dimension
        if (!runnerLocations.containsKey(dimension)) {
            return null;
        }

        return runnerLocations.get(dimension);
    }

    public void AddHunter(Player player) {

        // Add the player to the hunters team if the game is not running and the player is not already a hunter
        if (gameRunning || hunters.contains(player)) {
            return;
        }

        hunters.add(player);
    }

    public void SetRunner(Player player) {

        // Set the player as the runner if the game is not running
        if (gameRunning) {
            return;
        }

        runner = player;
    }

    public void clearTeams() {

        // Clear the runner and hunter teams
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
        // Run the compass updater every second
        Bukkit.getScheduler().runTaskTimer(pluginInstance, () -> {

            // Check if the game is running
            if (!this.IsGameRunning()) {
                return;
            }

            World.Environment runnerDimension = runner.getWorld().getEnvironment();
            this.updateRunnerLocation(runnerDimension, runner.getLocation());

            // Update the compass for each hunter based on the dimensions of the runner and hunter
            for (Player hunter : this.getHunters()) {

                World.Environment hunterDimension = hunter.getWorld().getEnvironment();

                // If the runner is in the overworld or nether and the hunter is in the overworld, update the normal compass with the last known location of the runner
                if ((runnerDimension == World.Environment.NORMAL || runnerDimension == World.Environment.NETHER) && hunterDimension == World.Environment.NORMAL) {
                    updateCompassInInventory(hunter, runner.getLocation());
                    hunter.setCompassTarget(this.getLastLocation(hunterDimension));
                }

                // If the runner and hunter are in the same dimension, update the compass with lodestone metadata
                if (runnerDimension == World.Environment.NETHER && hunterDimension == World.Environment.NETHER) {
                    updateCompassInInventory(hunter, runner.getLocation());
                }

                // If the runner and hunter are in the end dimension, update the compass with lodestone metadata
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

                    // Check if the hunter is in the overworld and update the compass to point to the runner's last known location (remove metadata for nether and end dimensions)
                    if (hunter.getWorld().getEnvironment() == World.Environment.NORMAL) {
                        compassMeta.setLodestone(null);
                        item.setItemMeta(compassMeta);
                        break;
                    }
                    // Check if the hunter is in the nether or end dimension and update the compass to point to the runner's last known location (with lodestone metadata)
                    else {

                        compassMeta.setLodestone(targetLocation);
                        compassMeta.setLodestoneTracked(false); // Set to false to avoid the compass being linked to a lodestone block

                        // Apply the modified meta back to the item
                        item.setItemMeta(compassMeta);
                        break;
                    }
                }
            }
        }
    }
}
