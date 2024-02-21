package org.machinemc.barebones.util;

import com.google.common.base.Preconditions;
import lombok.With;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

/**
 * An identifying object used to fetch and/or store unique objects,
 * NamespacedKey consists of namespace and key.
 * <p>
 * Valid characters for namespaces are [a-z0-9.-_].
 * <p>
 * Valid characters for keys are [a-z0-9.-_/].
 */
@With
public record NamespacedKey(String namespace, String key) {

    public static final String MINECRAFT_NAMESPACE = "minecraft";
    public static final String MACHINE_NAMESPACE = "machine";

    public NamespacedKey {
        Preconditions.checkNotNull(namespace, "Namespace can not be null");
        Preconditions.checkNotNull(key, "Key can not be null");
        if (!isValidNamespacedKey(namespace, key))
            throw new IllegalArgumentException(STR."The key '\{namespace}:\{key}' doesn't match the identifier format.");
    }

    /**
     * Creates new namespaced key from given namespace and key.
     * @param namespace namespace
     * @param key key
     * @return new namespaced key
     */
    @Contract("_, _ -> new")
    public static NamespacedKey of(final String namespace, final String key) {
        return new NamespacedKey(namespace, key);
    }

    /**
     * Parses the NamespacedKey from a String, namespace and key should
     * be separated by ':'.
     * @param namespacedKey String to parse as NamespacedKey
     * @return parsed NamespacedKey
     * @throws IllegalArgumentException if the input isn't a valid namespaced key
     */
    @Contract("_ -> new")
    public static NamespacedKey parse(final String namespacedKey) {
        return parseNamespacedKey(namespacedKey)
                .map(key -> NamespacedKey.of(key[0], key[1]))
                .orElseThrow(() -> new IllegalArgumentException(STR."The namespaced key '\{namespacedKey}' does not have a separator character ':'"));
    }

    /**
     * Parses the NamespacedKey from a String, namespace and key should
     * be separated by ':'.
     * @param namespacedKey String to parse as NamespacedKey
     * @return parsed NamespacedKey, or null if the input isn't a valid namespaced key
     */
    @Contract("_ -> new")
    public static Optional<NamespacedKey> parseSafe(final String namespacedKey) {
        try {
            return Optional.of(parse(namespacedKey));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Returns Namespaced key with 'minecraft' namespace.
     * @param key key of the NamespacedKey
     * @return minecraft NamespacedKey
     */
    @Contract("_ -> new")
    public static NamespacedKey minecraft(final String key) {
        return NamespacedKey.of(MINECRAFT_NAMESPACE, key);
    }

    /**
     * Returns Namespaced key with 'machine' namespace.
     * @param key key of the NamespacedKey
     * @return machine NamespacedKey
     */
    @Contract("_ -> new")
    public static NamespacedKey machine(final String key) {
        return NamespacedKey.of(MACHINE_NAMESPACE, key);
    }

    @Override
    @Contract(pure = true)
    public String toString() {
        return STR."\{namespace}:\{key}";
    }

    /**
     * Parses a string into a key-value pair.
     * <p>
     * This doesn't check if the pair follows the namespaced key format,
     * use {@link NamespacedKey#isValidNamespacedKey(String, String)} to check.
     * @param input the input
     * @return a string array where the first value is the namespace and the second value is the namespace,
     * or null if that input doesn't have a separator character ':'
     */
    static Optional<String[]> parseNamespacedKey(final String input) {
        Preconditions.checkNotNull(input, "Text to parse can not be null");
        final int separator = input.indexOf(':');
        if (separator == -1) return Optional.empty();
        return Optional.of(new String[]{input.substring(0, separator), input.substring(separator + 1)});
    }

    /**
     * Valid characters for namespaces are [a-z0-9.-_].
     * <p>
     * Valid characters for keys are [a-z0-9.-_/].
     * @param namespace the namespace
     * @param key the key
     * @return whether the namespace and key follow their formats
     */
    private static boolean isValidNamespacedKey(final String namespace, final String key) {
        if (namespace.isEmpty() || key.isEmpty())
            return false;
        for (final char c : namespace.toCharArray()) {
            if (!isValidNamespace(c))
                return false;
        }
        for (final char c : key.toCharArray()) {
            if (!isValidKey(c))
                return false;
        }
        return true;
    }

    /**
     * Valid characters for namespaces are [a-z0-9.-_].
     * @param c the character
     * @return whether character is allowed in a namespace
     */
    private static boolean isValidNamespace(final char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '.' || c == '-' || c == '_';
    }

    /**
     * Valid characters for keys are [a-z0-9.-_/].
     * @param c the character
     * @return whether character is allowed in a key
     */
    private static boolean isValidKey(final char c) {
        return isValidNamespace(c) || c == '/';
    }

}
