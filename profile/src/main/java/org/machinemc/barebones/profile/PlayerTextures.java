package org.machinemc.barebones.profile;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.gson.*;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
public record PlayerTextures(String value,
                             @Nullable String signature,
                             URL skinURL,
                             @Nullable URL capeURL,
                             @Nullable SkinModel skinModel) {

    /**
     * Name of the textures game profile property.
     */
    public static final String TEXTURES = "textures";

    public PlayerTextures {
        Preconditions.checkNotNull(value, "Value of the skin can not be null");
        Preconditions.checkNotNull(skinURL, "Skin url can not be null");
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
                .filter(p -> p.name().equals(TEXTURES))
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
        if (!property.name().equals(TEXTURES))
            throw new IllegalStateException("Provided property is not player textures property");
        return create(property.value(), property.signature());
    }

    /**
     * Creates player textures from value and signature.
     *
     * @param value value of the skin
     * @param signature signature of the skin
     * @return player textures from property
     * @throws MalformedURLException if the provided skin URL is malformed
     * @throws JsonSyntaxException if the value of the property is not a valid JSON format
     * @throws IllegalStateException if the provided property is not player textures property
     */
    public static PlayerTextures create(final String value, final @Nullable String signature) throws MalformedURLException, JsonSyntaxException {
        final JsonElement decoded = JsonParser.parseString(new String(Base64.getDecoder().decode(value)));
        if (!decoded.isJsonObject())
            throw new JsonSyntaxException("Texture value of the skin contains malformed JSON format");

        final JsonObject textures = decoded.getAsJsonObject().getAsJsonObject(TEXTURES);
        final JsonObject skinJson = textures.getAsJsonObject("SKIN");

        final URL skinURL = URI.create(skinJson.get("url").getAsString()).toURL();
        final URL capeURL = textures.has("CAPE")
                ? URI.create(textures.getAsJsonObject("CAPE").get("url").getAsString()).toURL()
                : null;

        SkinModel skinModel;
        try {
            skinModel = SkinModel.valueOf(skinJson.get("metadata")
                    .getAsJsonObject()
                    .get("model")
                    .getAsString()
                    .toUpperCase());
        } catch (Exception exception) {
            skinModel = null;
        }

        return new PlayerTextures(value, signature, skinURL, capeURL, skinModel);
    }

    /**
     * Creates new unsigned player textures from the given skin URL.
     *
     * @param skinURL skin URL
     * @return unsigned player textures with given skin
     * @throws MalformedURLException if the provided skin URL is malformed
     */
    public static PlayerTextures create(final URL skinURL) throws MalformedURLException {
        return create(createProperty(skinURL));
    }

    /**
     * Returns the player textures as game profile property.
     *
     * @return property for these textures
     */
    public GameProfile.Property asProperty() {
        return new GameProfile.Property(TEXTURES, value, signature);
    }

    private static final Gson GSON = new Gson();

    private static GameProfile.Property createProperty(final URL skinURL) {
        Preconditions.checkNotNull(skinURL, "Skin URL can not be null");
        final JsonObject value = new JsonObject();
        value.addProperty("signatureRequired", false);
        final JsonObject textures = new JsonObject();
        final JsonObject skin = new JsonObject();
        skin.addProperty("url", skinURL.toString());
        textures.add("SKIN", skin);
        value.add(TEXTURES, textures);
        final String valueJSON = GSON.toJson(value);
        final String valueString = BaseEncoding.base64().encode(valueJSON.getBytes(StandardCharsets.UTF_8));
        return new GameProfile.Property(TEXTURES, valueString, null);
    }

}
