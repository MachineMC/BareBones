package org.machinemc.barebones.util;

/**
 * Represents object that can be written to the {@link FriendlyByteBuf}.
 */
public interface Writable {

    /**
     * Writes this object to the friendly byte buffer.
     *
     * @param buf buffer to write into
     */
    void write(FriendlyByteBuf buf);

}
