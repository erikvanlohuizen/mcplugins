package nlerik.huntervsrunner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRunnerCommand implements CommandExecutor {
    private final GameManager gameManager;

    public JoinRunnerCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        gameManager.SetRunner(player);
        // Logic to handle joining the hunter team
        return true;
    }
}