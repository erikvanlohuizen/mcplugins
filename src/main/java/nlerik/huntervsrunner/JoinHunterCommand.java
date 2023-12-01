package nlerik.huntervsrunner;

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
        Player player = (Player) sender;
        gameManager.AddHunter(player);
        player.sendMessage("You have chosen Hunter!");
        // Logic to handle joining the hunter team
        return true;
    }
}
