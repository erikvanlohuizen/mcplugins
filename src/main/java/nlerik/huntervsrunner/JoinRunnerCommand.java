package nlerik.huntervsrunner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class JoinRunnerCommand implements CommandExecutor {
    private final GameManager gameManager;

    public JoinRunnerCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        gameManager.SetRunner(player);
        player.sendMessage("You have chosen Runner!");

        // Send a colored title when the player chooses Runner
        String title = ChatColor.BLUE + "Welcome " + ChatColor.GREEN + "Runner!";
        String subtitle = ChatColor.GOLD + "Prepare for the race!";

        // Send a colored title with fade-in, display, and fade-out times
        player.sendTitle(title, subtitle, 10, 70, 20);

        // Logic to handle joining the runner team
        return true;
    }
}