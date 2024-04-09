package nlerik.huntervsrunner;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GameListener implements Listener {
    private final GameManager gameManager;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        // Check if the game is running
        if (!gameManager.IsGameRunning()) { return; }

        // Check if the player is a hunter, if so add a compass to their inventory
        if (gameManager.getHunters().contains(event.getPlayer())) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
        }

        // Check if the player is the runner, if so stop the game
        if (gameManager.getRunner().getUniqueId() == event.getEntity().getUniqueId()) {
            gameManager.stopGame(false);
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {

        // Check if the game is running
        if (!gameManager.IsGameRunning()) { return; }

        // Check if the killer is a player and the current runner of the game
        if (event.getEntity().getKiller() != null && gameManager.getRunner().getUniqueId() == event.getEntity().getKiller().getUniqueId()) {

            // Check if the mob is an Enderman
            if (event.getEntity().getType() == EntityType.ENDERMAN) {

                // Add 1 ender pearl to the drops when the Enderman is killed by the runner
                event.getDrops().add(new ItemStack(Material.ENDER_PEARL, 1));
            }

            // Check if the mob is a Blaze
            if (event.getEntity().getType() == EntityType.BLAZE) {

                // Add 1 blaze rod to the drops when the Blaze is killed by the runner
                event.getDrops().add(new ItemStack(Material.BLAZE_ROD, 1));
            }

            // Check if the mob is an Ender dragon
            if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {

                gameManager.stopGame(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {

        // Check if the game is running
        if (!gameManager.IsGameRunning()) { return; }

        // Check if the player causing the portal event is the runner and execute the locate command to find the nearest fortress
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            Player player = event.getPlayer();

            if (gameManager.getRunner().getUniqueId() == player.getUniqueId()) {

                JavaPlugin plugin = JavaPlugin.getPlugin(HunterVsRunner.class);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        World world = player.getWorld();
                        Location structureLocation = world.locateNearestStructure(player.getLocation(), StructureType.NETHER_FORTRESS, 1000, true);

                        if (structureLocation != null) {
                            player.sendMessage("Nearest Nether Fortress is at: " + structureLocation.getBlockX() + ", " + structureLocation.getBlockY() + ", " + structureLocation.getBlockZ());
                        } else {
                            player.sendMessage("Could not find a Nether Fortress nearby.");
                        }
                    }
                }, 40L);
            }
        }
    }
}
