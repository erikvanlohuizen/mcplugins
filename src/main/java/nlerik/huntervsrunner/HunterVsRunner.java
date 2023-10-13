package nlerik.huntervsrunner;

import org.bukkit.plugin.java.JavaPlugin;
import me.lucko.luckperms.api.LuckPerms;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.cacheddata.CachedPermissionData;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.api.context.ContextBuilder;
import me.lucko.luckperms.api.context.ImmutableContextSet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class HunterVsRunner extends JavaPlugin {
//hoientest
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void createGroup(String groupName) {
        if (api == null) {
            getLogger().severe("LuckPerms not initialized.");
            return;
        }
}
