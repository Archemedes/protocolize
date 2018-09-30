package de.exceptionflug.protocolize.example;

import de.exceptionflug.protocolize.inventory.Inventory;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.inventory.InventoryType;
import de.exceptionflug.protocolize.inventory.event.InventoryClickEvent;
import de.exceptionflug.protocolize.inventory.packet.CloseWindow;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class InventoryExample implements Listener {

    public InventoryExample(final Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    public void openInventory(final ProxiedPlayer player) {
        // Creates a new chest inventory with 4 rows
        final Inventory inventory = new Inventory(36, new TextComponent("§9Inventory title"));

        // A simple item
        final ItemStack item = new ItemStack(ItemType.SIGN);
        item.setDisplayName("§6Click me");
        inventory.setItem(0, item);

        // Another simple item
        final ItemStack item2 = new ItemStack(ItemType.BLUE_WOOL);
        item2.setDisplayName("§aThis is a test");
        inventory.setItem(35, item2);

        InventoryModule.sendInventory(player, inventory);
    }

    private void openBrewingStandInventory(final ProxiedPlayer player) {
        // Creates a new brewing stand gui
        final Inventory brewingStand = new Inventory(InventoryType.BREWING_STAND, 0, new TextComponent("§5A brewing stand!"));

        // A simple item
        final ItemStack item = new ItemStack(ItemType.SIGN);
        item.setDisplayName("§6Click me");
        brewingStand.setItem(0, item);

        InventoryModule.sendInventory(player, brewingStand);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final ItemStack clicked = event.getClickedItem();
        final String title = event.getInventory().getTitle()[0].toLegacyText();
        final ProxiedPlayer player = event.getPlayer();
        if(title.equals("§9Inventory title")) {
            if(clicked.getType() == ItemType.SIGN) {
                player.sendMessage("§6Hihi, you clicked me!");
            } else if(clicked.getType() == ItemType.BLUE_WOOL) {
                openBrewingStandInventory(player);
            }
        } else if(title.equals("§5A brewing stand!")) {
            if(clicked.getType() == ItemType.SIGN) {
                player.unsafe().sendPacket(new CloseWindow(event.getWindowId())); // Closes the current gui
                player.sendMessage("§6You clicked me again :)");
            }
        }
    }

}
