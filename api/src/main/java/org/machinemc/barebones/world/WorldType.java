package org.machinemc.barebones.world;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * World type of server world.
 * Different world types have different properties on the client side.
 */
@Getter
@AllArgsConstructor
public enum WorldType {

    /**
     * Normal world type.
     */
    NORMAL(63),

    /**
     * Flat world type; used by super flat worlds.
     * Worlds with this type have different void fog and a horizon at y=0 instead of y=63.
     */
    FLAT(0);

    private final int voidFogLevel;

    /**
     * Returns world type of given name, ignores text case.
     *
     * @param name name of the world type
     * @return world type with given name
     */
    public static Optional<WorldType> getByName(final String name) {
        Preconditions.checkNotNull(name, "Name of the world type can not be null");
        for (final WorldType value : values())
            if (value.name().equalsIgnoreCase(name)) return Optional.of(value);
        return Optional.empty();
    }

}
