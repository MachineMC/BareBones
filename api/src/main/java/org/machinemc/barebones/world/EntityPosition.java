package org.machinemc.barebones.world;

import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Contract;
import org.machinemc.barebones.math.Vector2;
import org.machinemc.barebones.math.Vector3;

/**
 * Represent a position of an entity.
 */
@Data
@With
public class EntityPosition implements Cloneable {

    private final double x, y, z;
    private final float yaw, pitch;

    /**
     * Creates new position.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     * @param z z-coordinate of the position
     * @param yaw yaw of the position
     * @param pitch pitch of the position
     * @return new position
     */
    @Contract("_, _, _, _, _ -> new")
    public static EntityPosition of(final double x,
                                    final double y,
                                    final double z,
                                    final float yaw,
                                    final float pitch) {
        return new EntityPosition(x, y, z, yaw, pitch);
    }

    /**
     * Creates new position.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     * @param z z-coordinate of the position
     * @return new position
     */
    @Contract("_, _, _ -> new")
    public static EntityPosition of(final double x, final double y, final double z) {
        return new EntityPosition(x, y, z, 0, 0);
    }

    /**
     * Creates new entity position from block position.
     *
     * @param blockPosition block position
     * @return new position
     */
    @Contract("_ -> new")
    public static EntityPosition of(final BlockPosition blockPosition) {
        return new EntityPosition(blockPosition);
    }

    /**
     * Entity Position in a world.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     * @param z z-coordinate of the position
     * @param yaw yaw of the position
     * @param pitch pitch of the position
     */
    public EntityPosition(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = fixYaw(yaw);
        this.pitch = pitch;
    }

    /**
     * Entity Position in a world.
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     * @param z z-coordinate of the position
     */
    public EntityPosition(final double x, final double y, final double z) {
        this(x, y, z, 0, 0);
    }

    /**
     * Position in the world from a block position.
     * @param blockPosition block position of the entity position
     */
    public EntityPosition(final BlockPosition blockPosition) {
        this(blockPosition.x(), blockPosition.y(), blockPosition.z());
    }

    /**
     * Gets a unit-vector pointing in the direction that this position is
     * facing.
     *
     * @return a vector pointing the direction of this position's {@link #getPitch()}
     * and {@link #getYaw()}
     */
    public Vector3 getDirection() {
        Vector3 vector = new Vector3();
        final double rotX = this.getYaw();
        final double rotY = this.getPitch();

        vector = vector.withY(-Math.sin(Math.toRadians(rotY)));
        final double xz = Math.cos(Math.toRadians(rotY));

        vector = vector
                .withX(-xz * Math.sin(Math.toRadians(rotX)))
                .withZ(xz * Math.cos(Math.toRadians(rotX)));
        return vector;
    }

    /**
     * Sets the {@link #getYaw() yaw} and {@link #getPitch() pitch} to point
     * in the direction of the vector.
     *
     * @param vector the direction vector
     * @return copy of the position with updated yaw and pitch
     */
    @Contract(pure = true)
    public EntityPosition withDirection(final Vector3 vector) {
        final double pii = 2 * Math.PI;
        final double x = vector.x();
        final double z = vector.z();

        if (x == 0 && z == 0)
            return this.withPitch(vector.y() > 0 ? -90 : 90);

        final double theta = Math.atan2(-x, z);

        final double x2 = Math.pow(x, 2);
        final double z2 = Math.pow(z, 2);
        final double xz = Math.sqrt(x2 + z2);

        return this
                .withYaw((float) Math.toDegrees((theta + pii) % pii))
                .withPitch((float) Math.toDegrees(Math.atan(-vector.y() / xz)));
    }

    /**
     * Offsets the position by a vector.
     *
     * @param vector the vector
     * @return copy
     */
    @Contract(pure = true)
    public EntityPosition offset(final Vector3 vector) {
        return this
                .withX(x + vector.x())
                .withY(y + vector.y())
                .withZ(z + vector.z());
    }

    /**
     * @return x-coordinate of the position as whole number
     */
    public int getBlockX() {
        return (int) Math.floor(x);
    }

    /**
     * @return y-coordinate of the position as whole number
     */
    public int getBlockY() {
        return (int) Math.floor(y);
    }

    /**
     * @return z-coordinate of the position as whole number
     */
    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    /**
     * Converts the entity position to a block position.
     *
     * @return block position of this entity position
     */
    @Contract(pure = true)
    public BlockPosition toBlockPosition() {
        return new BlockPosition(getBlockX(), getBlockY(), getBlockZ());
    }

    /**
     * Converts this position to a vector.
     *
     * @return vector of this position
     */
    @Contract(pure = true)
    public Vector3 toVector() {
        return Vector3.of(getBlockX(), getBlockY(), getBlockZ());
    }

    /**
     * Converts rotation of the position to a vector.
     * @return vector with yaw and pitch
     */
    public Vector2 getRotationVector() {
        return Vector2.of(getYaw(), getPitch());
    }

    /**
     * Updates the rotation to value of given vector and returns a copy.
     *
     * @param rotation rotation vector
     * @return copy
     */
    @Contract(pure = true)
    public EntityPosition withRotation(final Vector2 rotation) {
        return this
                .withYaw((float) rotation.x())
                .withPitch((float) rotation.y());
    }

    /**
     * Fixes the yaw between -180 and 180 degrees.
     * @param yaw yaw
     * @return same yaw but between -180 and 180 degrees
     */
    private static float fixYaw(final float yaw) {
        float fixedYaw = yaw % 360;
        if (fixedYaw < -180.0F) {
            fixedYaw += 360.0F;
        } else if (fixedYaw > 180.0F) {
            fixedYaw -= 360.0F;
        }
        return fixedYaw;
    }

    /**
     * Checks whether the position is valid or not.
     * @param position position
     * @return if the position is valid
     */
    public static boolean isInvalid(final EntityPosition position) {
        final double x = position.getX(), y = position.getY(), z = position.getZ();
        if (!(Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z)))
            return true;
        return Math.max(Math.abs(x), Math.abs(z)) > 3.2e+7;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public EntityPosition clone() {
        return new EntityPosition(x, y, z, yaw, pitch);
    }

}
