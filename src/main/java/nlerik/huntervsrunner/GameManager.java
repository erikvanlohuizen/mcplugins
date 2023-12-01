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

    private Map<Integer, RunnerLocation> playerLocations = new HashMap<>();

    public boolean IsGameRunning() {
        return gameRunning;
    }

    public void SetGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void StopGame() {
        gameRunning = false;
    }

    public void updateRunnerLocation(Location location, int dimension) {
        RunnerLocation runnerLocation = new RunnerLocation(location);
        playerLocations.put(dimension, runnerLocation);
    }

    public RunnerLocation getLastLocation(int dimension) {

        RunnerLocation runnerLocation = playerLocations.get(dimension);
        return runnerLocation;
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

            this.updateRunnerLocation(runner.getLocation(), runner.getWorld().getEnvironment().getId());

            for (Player hunter : this.getHunters()) {
                RunnerLocation runnerLocation = this.getLastLocation(hunter.getWorld().getEnvironment().getId());
                if (runnerLocation == null) {
                    hunter.setCompassTarget(hunter.getLocation());
                } else {
                    hunter.setCompassTarget(runnerLocation.location);
                }
            }
        }, delay, delay);
    }
}
