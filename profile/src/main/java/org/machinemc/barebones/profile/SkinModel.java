package org.machinemc.barebones.profile;

import com.google.common.base.Preconditions;

import java.util.Optional;

/**
 * Represents the model of player's skin.
 */
public enum SkinModel {

    /**
     * Classic skin model.
     */
    CLASSIC,

    /**
     * Alex skin model with slim arms.
     */
    SLIM;

    /**
     * Returns skin model of given name.
     *
     * @param name name of the skin model
     * @return skin model with given name
     */
    public static Optional<SkinModel> getByName(final String name) {
        Preconditions.checkNotNull(name, "Name of the skin model can not be null");
        for (final SkinModel value : values())
            if (value.name().equalsIgnoreCase(name)) return Optional.of(value);
        return Optional.empty();
    }

}
