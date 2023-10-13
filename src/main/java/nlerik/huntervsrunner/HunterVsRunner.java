package nlerik.huntervsrunner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HunterVsRunner extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        gameManager = new GameManager();

        // Commands
        getCommand("joinhunter").setExecutor(new JoinHunterCommand(gameManager));

        Bukkit.getServer().getPluginManager().registerEvents(new GameListener(gameManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}

// Must haves
// Commands: join hunter, join runner, start game, stop game
// Start game: runner: speed 30sec, hunter: blindness 30sec, inv clear, max health, max food
// Join hunter: add to hunter team
// Join runner: add to runner team
// Stop game (if game is running): reset teams

// Game logic
// compass points to runner in every dimension or last known location in other dimension

// Nice to haves
// perks for hunter and runner
// multiple hunters
// nicer message visuals
// game ends automatically if runner dies or slays dragon
// commands clickable in chat