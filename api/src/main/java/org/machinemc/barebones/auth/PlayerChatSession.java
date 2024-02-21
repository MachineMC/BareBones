package org.machinemc.barebones.auth;

import com.google.common.base.Preconditions;

import java.security.PublicKey;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents session of a player used by the Minecraft chat system.
 *
 * @param uuid uuid of the session
 * @param publicKey public key of the session
 * @param signature signature of the session
 * @param timestamp expiration timestamp of the session
 */
public record PlayerChatSession(UUID uuid, PublicKey publicKey, byte[] signature, Instant timestamp) {

    public PlayerChatSession {
        Preconditions.checkNotNull(uuid, "UUID can not be null");
        Preconditions.checkNotNull(publicKey, "Public key can not be null");
        Preconditions.checkNotNull(signature, "Signature can not be null");
        Preconditions.checkNotNull(timestamp, "Timestamp can not be null");
    }

    /**
     * @return true if data are expired
     */
    public boolean hasExpired() {
        return timestamp.isBefore(Instant.now());
    }

}
