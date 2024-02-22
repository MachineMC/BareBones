package org.machinemc.barebones.auth;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.machinemc.barebones.entities.player.GameProfile;
import org.machinemc.barebones.entities.player.PlayerTextures;
import org.machinemc.barebones.utils.UUIDUtils;

import javax.annotation.Nullable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for communication with Mojang servers.
 */
public class AuthService {

    private final String authUrl;
    private final String userProfileUrl;
    private final String minecraftProfileUrl;

    /**
     * Creates new auth service from URLs strings that can be formatted later.
     *
     * @param authUrl auth url, e.g.: {@code https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s}
     * @param userProfileUrl user profile url, e.g.: {@code https://api.mojang.com/users/profiles/minecraft/%s}
     * @param minecraftProfileUrl minecraft profile url, e.g.: {@code https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false}
     */
    public AuthService(final String authUrl, final String userProfileUrl, final String minecraftProfileUrl) {
        this.authUrl = authUrl;
        this.userProfileUrl = userProfileUrl;
        this.minecraftProfileUrl = minecraftProfileUrl;
    }

    /**
     * Checks for auth data created by client for given server hash and returns
     * a game profile for the connection.
     * <p>
     * Returns empty optional if there is no active session for the server hash or
     * Mojang servers are unavailable.
     *
     * @param serverHash hash of the server, for more information see
     * {@link Crypt#getServerHash(String, java.security.PublicKey, javax.crypto.SecretKey)}
     * @param username username of the player
     * @return auth data
     */
    public CompletableFuture<Optional<GameProfile>> requestGameProfile(final String serverHash, final String username) throws URISyntaxException {
        Preconditions.checkNotNull(serverHash, "Server hash can not be null");
        Preconditions.checkNotNull(username, "Username can not be null");
        final URI url = new URI(String.format(authUrl, username, serverHash));
        return CompletableFuture.supplyAsync(() -> {
            final HttpRequest request = HttpRequest.newBuilder(url).GET().build();
            try (HttpClient client = HttpClient.newHttpClient()) {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != HttpURLConnection.HTTP_OK) return Optional.empty();
                final JsonObject json = (JsonObject) JsonParser.parseString(response.body());
                return Optional.of(parseGameProfile(json));
            } catch (Exception exception) {
                return Optional.empty();
            }
        });
    }

    /**
     * Returns online UUID of registered account.
     *
     * @param username username of the account
     * @return online UUID of account, empty if the account doesn't exist
     */
    public CompletableFuture<Optional<UUID>> getUUID(final @Nullable String username) {
        if (username == null) return CompletableFuture.completedFuture(Optional.empty());
        final String url = String.format(userProfileUrl, username);
        return CompletableFuture.supplyAsync(() -> {
            try {
                final HttpRequest request = HttpRequest.newBuilder(new URI(url))
                        .GET().build();
                try (HttpClient client = HttpClient.newHttpClient()) {
                    final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (!(response.statusCode() == HttpURLConnection.HTTP_OK)) return Optional.empty();
                    final JsonObject json = (JsonObject) JsonParser.parseString(response.body());
                    return UUIDUtils.parseUUID(json.get("id").getAsString());
                }
            } catch (Exception ignored) { }
            return Optional.empty();
        });
    }

    /**
     * Returns skin of registered account.
     *
     * @param uuid online uuid of the account
     * @return skin of the registered account, empty if the account doesn't exist
     */
    public CompletableFuture<Optional<PlayerTextures>> getSkin(final @Nullable UUID uuid) {
        if (uuid == null) return CompletableFuture.completedFuture(Optional.empty());
        final String url = String.format(minecraftProfileUrl, uuid);
        return CompletableFuture.supplyAsync(() -> {
            try {
                final HttpRequest request = HttpRequest.newBuilder(new URI(url))
                        .GET().build();
                try (HttpClient client = HttpClient.newHttpClient()) {
                    final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (!(response.statusCode() == HttpURLConnection.HTTP_OK)) return Optional.empty();
                    final JsonObject json = (JsonObject) JsonParser.parseString(response.body());
                    final GameProfile gameProfile = parseGameProfile(json);
                    return PlayerTextures.create(gameProfile);
                }
            } catch (Exception ignored) { }
            return Optional.empty();
        });
    }

    /**
     * Returns skin of registered account.
     *
     * @param username username of the account
     * @return skin of the registered account, empty if the account doesn't exist
     */
    public CompletableFuture<Optional<PlayerTextures>> getSkin(final @Nullable String username) {
        if (username == null) return CompletableFuture.completedFuture(Optional.empty());
        return getUUID(username).thenCompose(uuid -> getSkin(uuid.orElse(null)));
    }

    private static GameProfile parseGameProfile(final JsonObject json) {
        final UUID authUUID = UUIDUtils.parseUUID(json.get("id").getAsString()).orElseThrow();
        final String authUsername = json.get("name").getAsString();
        final List<GameProfile.Property> properties = new ArrayList<>();
        json.getAsJsonArray("properties").asList().stream()
                .map(JsonElement::getAsJsonObject)
                .forEach(property -> {
                    final String name = property.get("name").getAsString();
                    final String value = property.get("value").getAsString();
                    final String signature = property.has("signature")
                            ? property.get("signature").getAsString()
                            : null;
                    properties.add(new GameProfile.Property(name, value, signature));
                });
        return new GameProfile(authUUID, authUsername, properties);
    }

}
