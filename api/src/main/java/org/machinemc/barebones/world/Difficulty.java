package org.machinemc.barebones.world;

import com.google.common.base.Preconditions;

import java.util.Optional;

/**
 * Difficulty of the world.
 */
public enum Difficulty {

    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    /**
     * @return numeric id of the difficulty used by Minecraft protocol.
     */
    public int getID() {
        return ordinal();
    }

    /**
     * Returns difficulty from its numeric id.
     *
     * @param id id of the difficulty
     * @return difficulty for given id
     */
    public static Difficulty fromID(final int id) {
        Preconditions.checkArgument(id >= 0 && id < values().length, "Unsupported difficulty type");
        return values()[id];
    }

    /**
     * Returns difficulty of given name, ignores text case.
     *
     * @param name name of the difficulty
     * @return difficulty with given name
     */
    public static Optional<Difficulty> getByName(final String name) {
        Preconditions.checkNotNull(name, "Name of the difficulty can not be null");
        for (final Difficulty value : values())
            if (value.name().equalsIgnoreCase(name)) return Optional.of(value);
        return Optional.empty();
    }

}
