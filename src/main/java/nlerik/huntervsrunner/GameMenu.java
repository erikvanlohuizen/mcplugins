package nlerik.huntervsrunner;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class GameMenu implements CommandExecutor {
    public GameMenu(GameManager gameManager) {
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
        TextComponent option1 = createClickableText("Join the hunters", "/joinhunter");
        TextComponent option2 = createClickableText("Join the runners", "/joinrunner");
        TextComponent option3 = createClickableText("Start the game", "/startgame");

        // Stuur de klikbare teksten naar de speler
        player.spigot().sendMessage(option1, option2, option3);
    }

    private TextComponent createClickableText(String text, String command) {
        TextComponent clickableText = new TextComponent(ChatColor.YELLOW + text);
        clickableText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return clickableText;

    }
}