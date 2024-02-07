package nlerik.huntervsrunner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class GameMenuCommand implements CommandExecutor, Listener {
    private final GameManager gameManager;

    public GameMenuCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            giveMenuBook((Player) sender);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return false;
        }
    }

    private void giveMenuBook(Player player) {
        ItemStack menuBook = createMenuBook();
        player.getInventory().addItem(menuBook);
        player.sendMessage(ChatColor.GREEN + "You received the game menu book!");
    }

    private ItemStack createMenuBook() {
        ItemStack menuBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) menuBook.getItemMeta();
        bookMeta.setTitle("Game Menu");
        bookMeta.setAuthor("Server");

        TextComponent.Builder builder = Component.text().color(NamedTextColor.DARK_RED)
                .content("Game Menu\nClick the options below to perform actions:\n\n")
                .append(Component.text("[Join the hunters]").color(NamedTextColor.DARK_BLUE)
                        .clickEvent(ClickEvent.runCommand("/joinhunter"))
                        .hoverEvent(Component.text("Click to join the hunters").color(NamedTextColor.BLUE)))
                .append(Component.text("\n[Join the runners]").color(NamedTextColor.DARK_PURPLE)
                        .clickEvent(ClickEvent.runCommand("/joinrunner"))
                        .hoverEvent(Component.text("Click to join the runners").color(NamedTextColor.DARK_PURPLE)))
                .append(Component.text("\n[Start the game]").color(NamedTextColor.DARK_GREEN)
                        .clickEvent(ClickEvent.runCommand("/startgame"))
                        .hoverEvent(Component.text("Click to start the game").color(NamedTextColor.GREEN)));

        bookMeta.addPages(builder.build());
        menuBook.setItemMeta(bookMeta);
        return menuBook;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.WRITTEN_BOOK && item.hasItemMeta()) {
            BookMeta bookMeta = (BookMeta) item.getItemMeta();
            if (bookMeta != null && bookMeta.getTitle().equals("Game Menu")) {
                event.setCancelled(true);
            }
        }
    }
}