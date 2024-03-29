package org.machinemc.barebones.math;

import lombok.With;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 2D vector.
 *
 * @param x x component
 * @param y y component
 */
@With
public record Vector2(double x, double y) implements Cloneable {

    private static final Vector2 ZERO = new Vector2();
    private static final double EPSILON = 0.000001;

    /**
     *  Create a new 2D vector representing a zero vector.
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * Create a new 2D vector from another the other vector.
     * @param other the 2D vector
     */
    public Vector2(final Vector2 other) {
        this(other.x, other.y);
    }


    /**
     * Create a new 2D vector from a 3D vector.
     * @param other the 3D vector
     */
    public Vector2(final Vector3 other) {
        this(other.x(), other.y());
    }

    /**
     * Create a new 3D vector representing a zero vector.
     *
     * @return zero vector
     */
    public static Vector2 zero() {
        return Vector2.ZERO;
    }

    /**
     * Create a new 2D vector.
     *
     * @param x x
     * @param y y
     * @return vector
     */
    public static Vector2 of(final double x, final double y) {
        return new Vector2(x, y);
    }

    /**
     * Add the components of the other vector to copy of this one.
     *
     * @param other the 2D vector
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 add(final Vector2 other) {
        return this.withX(x + other.x).withY(y + other.y);
    }

    /**
     * Subtract the components of the other vector from copy of this one.
     *
     * @param other the 2D vector
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 subtract(final Vector2 other) {
        return this.withX(x - other.x).withY(y - other.y);
    }

    /**
     * Multiply the components of the other vector by this one and returns a copy.
     *
     * @param other the 2D vector
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 multiply(final Vector2 other) {
        return this.withX(x * other.x).withY(y * other.y);
    }

    /**
     * Multiply the components of this vector by the scalar.
     *
     * @param scalar the scalar
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 multiply(final double scalar) {
        return multiply(new Vector2(scalar, scalar));
    }

    /**
     * Divide the components of the other vector by this one.
     *
     * @param other the 2D vector
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 divide(final Vector2 other) {
        return this.withX(x / other.x).withY(y / other.y);
    }

    /**
     * Divide the components of this vector by the scalar.
     *
     * @param scalar the scalar
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 divide(final double scalar) {
        return divide(new Vector2(scalar, scalar));
    }

    /**
     * Returns the length of this vector squared.
     * This is used as an optimization to avoid multiple sqrt when a single sqrt will do.
     *
     * @return the length of this vector squared
     */
    public double lengthSquared() {
        return x * x + y * y;
    }

    /**
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Calculates squared distance between two vectors.
     *
     * @param other other vector
     * @return squared distance
     */
    public double distanceSquared(final Vector2 other) {
        return Math.pow(x, other.x) - Math.pow(y, other.y);
    }

    /**
     * Calculates distance between two vectors.
     *
     * @param other other vector
     * @return distance
     */
    public double distance(final Vector2 other) {
        return Math.sqrt(distanceSquared(other));
    }

    /**
     * Returns a copy of this normalized vector.
     *
     * @return copy
     */
    @Contract(pure = true)
    public Vector2 normalize() {
        return multiply(1 / length());
    }

    /**
     * Compute the dot product of this vector and the other vector.
     *
     * @param other the other vector
     * @return the dot product
     */
    public double dot(final Vector2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Compute the cross product of this vector and the other vector.
     *
     * @param other the other vector
     * @return the cross product scalar
     */
    public double crossProduct(final Vector2 other) {
        return this.x * other.y - other.x * this.y;
    }


    /**
     * Calculates the angle between this vector and the other vector.
     *
     * @param other the other vector
     * @return the angle
     */
    public double angle(final Vector2 other) {
        return Math.acos(dot(other) / Math.sqrt(lengthSquared() * other.lengthSquared()));
    }

    /**
     * Sets this vector to the midpoint between this vector and another.
     *
     * @param other The other vector
     * @return this
     */
    @Contract(pure = true)
    public Vector2 midpoint(final Vector2 other) {
        return this.withX((x + other.x) / 2).withY((y + other.y) / 2);
    }

    /**
     * Returns whether this vector is in an axis-aligned bounding box.
     * <p>
     * The minimum and maximum vectors given must be truly the minimum and
     * maximum X, Y and Z components.
     * @param min Minimum vector
     * @param max Maximum vector
     * @return whether this vector is in the AABB
     */
    public boolean isInAABB(final Vector2 min, final Vector2 max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y;
    }

    /**
     * Returns whether this vector is within a sphere.
     * @param origin Sphere origin.
     * @param radius Sphere radius.
     * @return whether this vector is in the sphere.
     */
    public boolean isInSphere(final Vector2 origin, final double radius) {
        return (Math.pow(origin.x - x, 2) + Math.pow(origin.y - y, 2)) <= Math.pow(radius, 2);
    }

    /**
     * @return if the vector is normalized
     */
    public boolean isNormalized() {
        return Math.abs(lengthSquared() - 1) < EPSILON;
    }

    /**
     * @return x component of the vector as whole number
     */
    public int getBlockX() {
        return (int) Math.floor(x);
    }

    /**
     * @return y component of the vector as whole number
     */
    public int getBlockY() {
        return (int) Math.floor(y);
    }

    /**
     * Gets a random vector with components having a random value between 0
     * and 1.
     * @return A random vector
     */
    public static Vector2 getRandom() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return new Vector2(random.nextDouble(), random.nextDouble());
    }

    /**
     * Checks to see if two objects are equal.
     * <p>
     * Only two Vectors can ever return true. This method uses a fuzzy match
     * to account for floating point errors. The epsilon can be retrieved
     * with epsilon.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Vector2 other))
            return false;
        return Math.abs(x - other.x) < EPSILON && Math.abs(y - other.y) < EPSILON;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.x));
        hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.y));
        return hash;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector2 clone() {
        return new Vector2(x, y);
    }

}
