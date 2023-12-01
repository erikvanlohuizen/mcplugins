package nlerik.huntervsrunner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
                    hunter.setCompassTarget(hunter.getLocation());
                } else {
                    hunter.setCompassTarget(runnerLocation);
                }
            }
        }, delay, delay);
    }
}
