package nlerik.huntervsrunner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StopGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            if (gameManager.IsGameRunning()) {
                gameManager.stopGame(false);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(
                            ChatColor.RED + "Game Stopped",
                            ChatColor.YELLOW + "Stopped by " + player.getName(),
                            10, 70, 20
                    );
                }


            } else {
                player.sendMessage(ChatColor.YELLOW + "No game is currently running.");
            }
            return true;
        } else {
            // Console kan ook stoppen
            if (gameManager.IsGameRunning()) {
                gameManager.stopGame(false);
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "The game has been stopped by the console!");

            } else {
                System.out.println("No game is currently running.");
            }
            return true;
        }
    }
}
