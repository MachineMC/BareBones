package org.machinemc.barebones.world;

import lombok.With;
import org.jetbrains.annotations.Contract;
import org.machinemc.barebones.math.Vector3;

/**
 * Represents position of a block in the world.
 *
 * @param x x-coordinate
 * @param y y-coordinate
 * @param z z-coordinate
 */
@With
public record BlockPosition(int x, int y, int z) implements Cloneable {

    public static final long PACKED_X_MASK = 0x3FFFFFF; // max x-coordinate value
    public static final long PACKED_Y_MASK = 0xFFF; // max y-coordinate value
    public static final long PACKED_Z_MASK = 0x3FFFFFF; // max z-coordinate value

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return block position from given coordinates
     */
    @Contract("_, _, _ -> new")
    public static BlockPosition of(final int x, final int y, final int z) {
        return new BlockPosition(x, y, z);
    }

    /**
     * Converts the block position to a vector.
     *
     * @return vector of this block position
     */
    public Vector3 toVector() {
        return Vector3.of(x, y, z);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public BlockPosition clone() {
        return new BlockPosition(x, y, z);
    }

}
