package de.exceptionflug.protocolize.items;

import com.google.common.collect.Maps;
import de.exceptionflug.protocolize.items.packet.HeldItemChange;
import de.exceptionflug.protocolize.items.packet.SetSlot;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PlayerInventory {

    private final Map<Integer, ItemStack> map = Maps.newHashMap();
    private short heldItem;
    private final UUID uuid;

    public PlayerInventory(final UUID uuid) {
        this.uuid = uuid;
    }

    public boolean setItem(final int slot, final ItemStack stack) {
        if(getItem(slot) != null) {
            if(getItem(slot).isHomebrew())
                return false;
        }
        map.put(slot, stack);
        return true;
    }

    public boolean removeItem(final int slot) {
        map.remove(slot);
        return true;
    }

    public ItemStack getItem(final int slot) {
        return map.get(slot);
    }

    public void update() {
//        final WindowItems items = new WindowItems((short) 0, Lists.newArrayList());
//        for (int i = 0; i <= 44; i++) {
//            ItemStack stack = getItem(i);
//            if(stack == null) {
//                stack = ItemStack.NO_DATA;
//            }
//            items.getItems().add(stack);
//        }
//        if(getPlayer() == null) return;
//        getPlayer().unsafe().sendPacket(items);
        for (int i = 0; i <= 44; i++) {
            final ItemStack stack = getItem(i);
            if(stack == null || stack.getType() == ItemType.NO_DATA) {
                continue;
            }
            getPlayer().unsafe().sendPacket(new SetSlot((byte) 0, (short) i, stack));
        }

    }

    public List<ItemStack> getItemsIndexed() {
        final ItemStack[] outArray = new ItemStack[46];
        for(final Integer id : map.keySet()) {
            outArray[id] = map.get(id);
        }
        return Arrays.asList(outArray);
    }

    public List<ItemStack> getItemsIndexedContainer() {
        final ItemStack[] outArray = new ItemStack[45-9];
        for(final Integer id : map.keySet()) {
            if(id < 9 || id == 45)
                continue;
            outArray[id-9] = map.get(id);
        }
        return Arrays.asList(outArray);
    }

    public ProxiedPlayer getPlayer() {
        return ProxyServer.getInstance().getPlayer(uuid);
    }

    public short getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(final short heldItem) {
        this.heldItem = heldItem;
    }

    public void changeHeldItem(final short rawSlot) {
        setHeldItem((short) (rawSlot+36));
        if(getPlayer() == null) return;
        getPlayer().unsafe().sendPacket(new HeldItemChange(rawSlot));
        getPlayer().getServer().unsafe().sendPacket(new HeldItemChange(rawSlot));
    }

}
