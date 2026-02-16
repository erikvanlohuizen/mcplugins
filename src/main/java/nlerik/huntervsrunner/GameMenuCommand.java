package nlerik.huntervsrunner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class GameMenuCommand implements CommandExecutor {

    private final GameManager gameManager;

    // Constructor die GameManager accepteert
    public GameMenuCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Dit command kan alleen in-game worden uitgevoerd!");
            return true;
        }

        Player player = (Player) sender;

        // Geef het menu-boek
        ItemStack menuBook = createMenuBook();
        player.getInventory().addItem(menuBook);
        player.sendMessage(ChatColor.GREEN + "Je hebt het Game Menu boek ontvangen!");
        return true;
    }

    private ItemStack createMenuBook() {
        ItemStack menuBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) menuBook.getItemMeta();

        if (bookMeta != null) {
            bookMeta.setTitle("Game Menu");
            bookMeta.setAuthor("Server");

            // Eenvoudige pagina met kleuren, zonder Kyori
            String page = ChatColor.DARK_RED + "Game Menu\n\n"
                    + ChatColor.BLUE + "[Join Hunters] " + ChatColor.WHITE + "- gebruik /joinhunter\n"
                    + ChatColor.DARK_PURPLE + "[Join Runners] " + ChatColor.WHITE + "- gebruik /joinrunner\n"
                    + ChatColor.DARK_GREEN + "[Start Game] " + ChatColor.WHITE + "- gebruik /startgame";

            bookMeta.addPage(page);
            menuBook.setItemMeta(bookMeta);
        }

        return menuBook;
    }
}
