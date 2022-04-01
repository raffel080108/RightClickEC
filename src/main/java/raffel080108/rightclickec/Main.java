package raffel080108.rightclickec;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getScheduler;
import static raffel080108.rightclickec.Actions.openEnderChest;

public final class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Actions(), this);

        getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers())
                if  (!player.getOpenInventory().getType().equals(InventoryType.ENDER_CHEST))
                    openEnderChest.remove(player);
        }, 0L, 1L);

        getLogger().info("The RightClickEC plugin has started!");
    }

    @Override
    public void onDisable() {
        getLogger().info("The RightClickEC plugin has stopped!");
    }
}