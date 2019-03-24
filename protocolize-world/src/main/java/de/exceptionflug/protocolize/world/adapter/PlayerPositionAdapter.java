package de.exceptionflug.protocolize.world.adapter;

import de.exceptionflug.protocolize.api.event.PacketReceiveEvent;
import de.exceptionflug.protocolize.api.handler.PacketAdapter;
import de.exceptionflug.protocolize.api.protocol.Stream;
import de.exceptionflug.protocolize.world.Location;
import de.exceptionflug.protocolize.world.WorldModule;
import de.exceptionflug.protocolize.world.packet.PlayerLook;
import de.exceptionflug.protocolize.world.packet.PlayerPosition;
import net.md_5.bungee.api.ProxyServer;

public class PlayerPositionAdapter extends PacketAdapter<PlayerPosition> {

    public PlayerPositionAdapter() {
        super(Stream.UPSTREAM, PlayerPosition.class);
    }

    @Override
    public void receive(final PacketReceiveEvent<PlayerPosition> event) {
        final PlayerPosition packet = event.getPacket();
        final Location location = WorldModule.getLocation(event.getPlayer().getUniqueId());
        if(location == null) {
            WorldModule.setLocation(event.getPlayer().getUniqueId(), packet.getLocation());
            return;
        }
        location.setX(packet.getLocation().getX());
        location.setY(packet.getLocation().getY());
        location.setZ(packet.getLocation().getZ());
    }

}
