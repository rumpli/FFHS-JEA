package ch.ffhs.blogrest;

public record NamedEntityReference<KeyType>(KeyType key, String displayName) {
}
