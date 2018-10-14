package de.exceptionflug.protocolize.world.packet;

import de.exceptionflug.protocolize.api.protocol.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static de.exceptionflug.protocolize.api.util.ProtocolVersions.*;

public class ChangeGameState extends AbstractPacket {

    public static Map<Integer, Integer> MAPPING = new HashMap<>();

    static {
        MAPPING.put(MINECRAFT_1_8, 0x2B);
        MAPPING.put(MINECRAFT_1_9, 0x1E);
        MAPPING.put(MINECRAFT_1_9_1, 0x1E);
        MAPPING.put(MINECRAFT_1_9_2, 0x1E);
        MAPPING.put(MINECRAFT_1_9_3, 0x1E);
        MAPPING.put(MINECRAFT_1_10, 0x1E);
        MAPPING.put(MINECRAFT_1_11, 0x1E);
        MAPPING.put(MINECRAFT_1_11_1, 0x1E);
        MAPPING.put(MINECRAFT_1_12, 0x1E);
        MAPPING.put(MINECRAFT_1_12_1, 0x1E);
        MAPPING.put(MINECRAFT_1_12_2, 0x1E);
        MAPPING.put(MINECRAFT_1_13, 0x20);
        MAPPING.put(MINECRAFT_1_13_1, 0x20);
    }

    private Reason reason;
    private float value;

    public ChangeGameState(final Reason reason, final float value) {
        this.reason = reason;
        this.value = value;
    }

    public ChangeGameState() {

    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(final Reason reason) {
        this.reason = reason;
    }

    public float getValue() {
        return value;
    }

    public void setValue(final float value) {
        this.value = value;
    }

    @Override
    public void read(final ByteBuf buf, final Direction direction, final int protocolVersion) {
        reason = Reason.values()[buf.readUnsignedByte()];
        value = buf.readFloat();
    }

    @Override
    public void write(final ByteBuf buf, final Direction direction, final int protocolVersion) {
        buf.writeByte(((byte)reason.ordinal()) & 0xFF);
        buf.writeFloat(value);
    }

    @Override
    public String toString() {
        return "ChangeGameState{" +
                "reason=" + reason +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeGameState that = (ChangeGameState) o;
        return Float.compare(that.value, value) == 0 &&
                reason == that.reason;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, value);
    }

    public enum Reason {

        INVALID_BED, END_RAINING, BEGIN_RAINING, CHANGE_GAMEMODE, EXIT_END, DEMO_MESSAGE, ARROW_HITTING_PLAYER, FADE_VALUE, FADE_TIME, ELDER_GUARDIAN_APPEARANCE

    }

}
