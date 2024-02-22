package org.machinemc.barebones.entities.player;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;

/**
 * Represents player's skin textures.
 * @param value texture value
 * @param signature signature of the texture
 * @param skinURL url for the skin
 * @param capeURL url for the cape
 * @param skinModel model of the skin
 */
public record PlayerTextures(String value, @Nullable String signature, URL skinURL, @Nullable URL capeURL, SkinModel skinModel) {

    public PlayerTextures {
        Preconditions.checkNotNull(value, "Value of the skin can not be null");
        Preconditions.checkNotNull(skinURL, "Skin url can not be null");
        Preconditions.checkNotNull(skinModel, "Skin model can not be null");
    }

    /**
     * Creates player textures from GameProfile.
     *
     * @param gameProfile GameProfile
     * @return player textures from GameProfile
     * @throws MalformedURLException if the provided skin URL is malformed
     * @throws JsonSyntaxException if the value of the property is not a valid JSON format
     */
    public static Optional<PlayerTextures> create(final GameProfile gameProfile) throws MalformedURLException, JsonSyntaxException {
        final GameProfile.Property property = gameProfile.properties().stream()
                .filter(p -> p.name().equals("textures"))
                .findFirst()
                .orElse(null);
        if (property == null) return Optional.empty();
        return Optional.of(create(property));
    }

    /**
     * Creates player textures from GameProfile property.
     *
     * @param property property
     * @return player textures from property
     * @throws MalformedURLException if the provided skin URL is malformed
     * @throws JsonSyntaxException if the value of the property is not a valid JSON format
     * @throws IllegalStateException if the provided property is not player textures property
     */
    public static PlayerTextures create(final GameProfile.Property property) throws MalformedURLException, JsonSyntaxException {
        if (!property.name().equals("textures"))
            throw new IllegalStateException("Provided property is not player textures property");

        final JsonElement decoded = JsonParser.parseString(new String(Base64.getDecoder().decode(property.value())));
        if (!decoded.isJsonObject())
            throw new JsonSyntaxException("Texture value of the skin contains malformed JSON format");

        final JsonObject textures = decoded.getAsJsonObject().getAsJsonObject("textures");
        final JsonObject skinJson = textures.getAsJsonObject("SKIN");

        final URL skinURL = URI.create(skinJson.get("url").getAsString()).toURL();
        final URL capeURL = textures.has("CAPE")
                ? URI.create(textures.getAsJsonObject("CAPE").get("url").getAsString()).toURL()
                : null;

        final SkinModel skinModel = skinJson.has("metadata")
                ? SkinModel.getByName(skinJson.get("metadata")
                .getAsJsonObject()
                .get("model")
                .getAsString()
                .toUpperCase())
                .orElseThrow()
                : SkinModel.CLASSIC;

        return new PlayerTextures(property.value(), property.signature(), skinURL, capeURL, skinModel);
    }

}
