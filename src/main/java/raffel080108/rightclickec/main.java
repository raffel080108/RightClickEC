package raffel080108.rightclickec;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

import static raffel080108.rightclickec.RightClickActions.openEnderChest;

public final class main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new RightClickActions(), this);

        @SuppressWarnings("unused")
        BukkitTask removePlayer = new RemovePlayerTask().runTaskTimer(this, 0L, 1L);

        getLogger().info("RightClickEC plugin has started");
    }

    @Override
    public void onDisable() {
        getLogger().info("RightClickEC plugin has stopped");
    }
}

class RightClickActions implements Listener {
    static ArrayList<Player> openEnderChest = new ArrayList<>();

    @EventHandler
    public void rightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.ENDER_CHEST) && event.getAction().equals(Action.RIGHT_CLICK_AIR) && !openEnderChest.contains(player)) {
            event.setCancelled(true);
            openEnderChest.add(player);
            Inventory enderChest = Bukkit.createInventory(player, InventoryType.ENDER_CHEST, "Ender Chest");
            enderChest.setContents(player.getEnderChest().getContents());
            player.openInventory(enderChest);
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f);
        }
    }

    @EventHandler
    public void rightClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();

        if (event.isRightClick() && item != null && inventory != null) {
            if (item.getType().equals(Material.ENDER_CHEST) && !openEnderChest.contains(player) && inventory.equals(event.getView().getBottomInventory())) {
                event.setCancelled(true);
                openEnderChest.add(player);
                Inventory enderChest = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Ender Chest");
                enderChest.setContents(player.getEnderChest().getContents());
                player.openInventory(enderChest);
                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f);
            }
        }
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        if (openEnderChest.contains(player) && inventory.getType().equals(InventoryType.ENDER_CHEST))
            player.getEnderChest().setContents(inventory.getContents());
    }
}

class RemovePlayerTask extends BukkitRunnable {
    public RemovePlayerTask() {}

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers())
            if  (!player.getOpenInventory().getType().equals(InventoryType.ENDER_CHEST))
                openEnderChest.remove(player);
    }
}