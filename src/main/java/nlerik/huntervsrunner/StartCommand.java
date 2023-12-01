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
            for (Player player : hunters) {
                player.setGameMode(GameMode.SURVIVAL);
                //PotionEffect blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, 600, 0);
                //player.addPotionEffect(blindnessEffect);
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



gameManager.SetGameRunning(true);
        return true;
    }
}