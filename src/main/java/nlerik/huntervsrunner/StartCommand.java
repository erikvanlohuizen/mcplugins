package nlerik.huntervsrunner;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.List;

public class StartCommand implements CommandExecutor {
    private final GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Logic to handle joining the hunter team
        List<Player> hunters = gameManager.getHunters();
        World world = hunters.get(0).getWorld(); // Assuming the hunters are in the same world

        // Set the difficulty to NORMAL
        world.setDifficulty(org.bukkit.Difficulty.NORMAL);
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
                String subtitle = ChatColor.GOLD + "Hunters, the game has begun!";
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