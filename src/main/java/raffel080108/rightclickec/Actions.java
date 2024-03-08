/*
 RightClickEC © 2022 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

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

import java.util.ArrayList;

public class Actions implements Listener {
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
