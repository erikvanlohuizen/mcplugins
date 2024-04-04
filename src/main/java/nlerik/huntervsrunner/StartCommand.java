package nlerik.huntervsrunner;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class StartCommand implements CommandExecutor {
    private final GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<Player> hunters = gameManager.getHunters();

        // Set the difficulty of each world to NORMAL
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.NORMAL);
        }

        for (Player player : hunters) {
            player.setGameMode(GameMode.SURVIVAL);
            PotionEffect blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, 600, 0);
            player.addPotionEffect(blindnessEffect);
            player.sendMessage("Let the game begin!");
            player.setFoodLevel(20);
            player.setHealth(20);
            player.setSaturation(20);
            Inventory playerInventory = player.getInventory();
            playerInventory.clear();
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta compassMeta = compass.getItemMeta();
            compassMeta.setDisplayName("Hunter's Compass");
            compass.setItemMeta(compassMeta);
            player.getInventory().addItem(compass);
            String title = ChatColor.RED + "Game Start!";
            String subtitle = ChatColor.RED + "Hunters, the game has begun!";
            player.sendTitle(title, subtitle, 10, 70, 20);
        }

        // Logic to handle joining the runner team
        Player runner = gameManager.getRunner();
        runner.setGameMode(GameMode.SURVIVAL);
        runner.sendMessage("Let the game begin!");
        PotionEffect swiftnessEffect = new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1, true, false);
        runner.addPotionEffect(swiftnessEffect);
        runner.setFoodLevel(20);
        runner.setHealth(20);
        runner.setSaturation(20);
        Inventory runnerInventory = runner.getInventory();
        runnerInventory.clear();
        String runnerTitle = ChatColor.BLUE + "Game Start!";
        String runnerSubtitle = ChatColor.GREEN + "Runner, the game has begun!";
        runner.sendTitle(runnerTitle, runnerSubtitle, 10, 70, 20);

        gameManager.SetGameRunning(true);
        return true;
    }
}