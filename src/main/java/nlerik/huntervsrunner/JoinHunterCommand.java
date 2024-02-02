package nlerik.huntervsrunner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class JoinHunterCommand implements CommandExecutor {
    private final GameManager gameManager;

    public JoinHunterCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        gameManager.AddHunter(player);
        player.sendMessage("You have chosen Hunter!");

        // Send a colored title when the player chooses Hunter
        String title = ChatColor.GREEN + "Welcome " + ChatColor.YELLOW + "Hunter!";
        String subtitle = ChatColor.AQUA + "Prepare for the hunt!";

        // Send a colored title with fade-in, display, and fade-out times
        player.sendTitle(title, subtitle, 10, 70, 20);

        // Logic to handle joining the hunter team
        return true;
    }
}
