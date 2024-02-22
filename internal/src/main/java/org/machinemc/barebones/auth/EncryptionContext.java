package org.machinemc.barebones.auth;

import com.google.common.base.Preconditions;

import javax.crypto.Cipher;

/**
 * Represents context for encrypted client connection.
 *
 * @param encrypt encryption cipher
 * @param decrypt decryption cipher
 */
public record EncryptionContext(Cipher encrypt, Cipher decrypt) {

    /**
     * Encrypts provided data using this encryption context.
     *
     * @param data data to encrypt
     * @return encrypted data
     */
    public byte[] encrypt(final byte[] data) {
        Preconditions.checkNotNull(data, "Data to encrypt can not be null");
        return encrypt.update(data);
    }

    /**
     * Decrypts provided data using this encryption context.
     *
     * @param data data to decrypt
     * @return decrypted data
     */
    public byte[] decrypt(final byte[] data) {
        Preconditions.checkNotNull(data, "Data to decrypt can not be null");
        return decrypt.update(data);
    }

}
