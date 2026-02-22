package nlerik.huntervsrunner;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private boolean gameRunning = false;
    private Player runner;
    private List<Player> hunters = new ArrayList<>();
    private BukkitTask compassTask;


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

        // Stop compass updater
        if (compassTask != null) {
            compassTask.cancel();
            compassTask = null;
        }

        // Stuur win message
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (runnerWon) {
                player.sendMessage(ChatColor.GREEN + "The runner has won the game!");
            } else {
                player.sendMessage(ChatColor.RED + "The hunters have won the game!");
            }

            player.setFoodLevel(20);
            player.setHealth(20);
            player.getActivePotionEffects()
                    .forEach(effect -> player.removePotionEffect(effect.getType()));
        }

        // Cleanup state
        runnerLocations.clear();
        clearTeams();
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
        if (gameRunning) {
            player.sendMessage(ChatColor.RED + "Cannot change roles while game is running!");
            return;
        }

        // Remove from runner if needed
        if (player.equals(runner)) {
            runner = null;
        }

        // Prevent duplicate
        if (!hunters.contains(player)) {
            hunters.add(player);
            player.sendMessage(ChatColor.GREEN + "You are now a Hunter!");
        }
    }

    public void SetRunner(Player player) {
        if (gameRunning) {
            player.sendMessage(ChatColor.RED + "Cannot change roles while game is running!");
            return;
        }

        // Remove from hunters if needed
        hunters.remove(player);

        // Optionally notify old runner
        if (runner != null && !runner.equals(player)) {
            runner.sendMessage(ChatColor.RED + "You are no longer the Runner!");
        }

        runner = player;
        player.sendMessage(ChatColor.AQUA + "You are now the Runner!");
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

                if (runnerDimension == World.Environment.NORMAL && hunterDimension == World.Environment.NETHER) {
                    updateCompassInInventory(hunter, this.getLastLocation(hunterDimension));
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
