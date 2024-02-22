package org.machinemc.barebones.world;

import com.google.common.base.Preconditions;

import java.util.Optional;

/**
 * Representing player's gamemode.
 */
public enum GameMode {

    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    /**
     * @return numeric id of the gamemode used by Minecraft protocol.
     */
    public int getID() {
        return ordinal();
    }

    /**
     * Returns gamemode from its numeric id.
     * @param id id of the gamemode
     * @return gamemode for given id
     */
    public static GameMode fromID(final int id) {
        Preconditions.checkArgument(id >= 0 && id < values().length, "Unsupported Gamemode type");
        return values()[id];
    }

    /**
     * Returns gamemode from its numeric id, supporting -1 as null value.
     * @param id id of the gamemode
     * @return gamemode for given id
     */
    public static Optional<GameMode> fromNullableID(final int id) {
        if (id == -1) return Optional.empty();
        return Optional.of(fromID(id));
    }

    /**
     * Returns gamemode of given name.
     * @param name name of the gamemode
     * @return gamemode with given name
     */
    public static Optional<GameMode> getByName(final String name) {
        Preconditions.checkNotNull(name, "Name of the gamemode can not be null");
        for (final GameMode value : values())
            if (value.name().equalsIgnoreCase(name)) return Optional.of(value);
        return Optional.empty();
    }

}
