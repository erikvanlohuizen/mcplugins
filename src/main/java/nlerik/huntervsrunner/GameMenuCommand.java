package nlerik.huntervsrunner;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class GameMenuCommand implements CommandExecutor {
    private final GameManager gameManager;

    public GameMenuCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            openMenu((Player) sender);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return false;
        }
    }
    private void openMenu(Player player) {
        // Implement your chat-based menu logic here
        player.sendMessage(ChatColor.GREEN + "Click here to open the menu: ");
        TextComponent option1 = createClickableText("Join the hunters\n", "/joinhunter");
        TextComponent option2 = createClickableText("Join the runners\n", "/joinrunner");
        TextComponent option3 = createClickableText("Start the game\n", "/startgame");

        // Stuur de klikbare teksten naar de speler
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(option1, option2, option3);
        }
    }
    private TextComponent createClickableText(String text, String command) {
        TextComponent clickableText = new TextComponent(ChatColor.YELLOW + text);
        clickableText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return clickableText;
    }

}