package de.exceptionflug.protocolize.items.adapter;

import de.exceptionflug.protocolize.api.event.PacketReceiveEvent;
import de.exceptionflug.protocolize.api.handler.PacketAdapter;
import de.exceptionflug.protocolize.api.protocol.Stream;
import de.exceptionflug.protocolize.items.InventoryManager;
import de.exceptionflug.protocolize.items.PlayerInventory;
import de.exceptionflug.protocolize.items.packet.HeldItemChange;

public class HeldItemChangeUpstreamAdapter extends PacketAdapter<HeldItemChange> {

    public HeldItemChangeUpstreamAdapter() {
        super(Stream.UPSTREAM, HeldItemChange.class);
    }

    @Override
    public void receive(final PacketReceiveEvent<HeldItemChange> event) {
        final PlayerInventory inventory = InventoryManager.getInventory(event.getPlayer().getUniqueId());
        inventory.setHeldItem(event.getPacket().getNewSlot());
    }
}
