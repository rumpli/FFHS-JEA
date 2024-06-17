package ch.ffhs.jea.auth;

public class NamedEntityReference<KeyType> {
    private KeyType key;
    private String displayName;

    public NamedEntityReference() {
    }

    public NamedEntityReference(KeyType key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public KeyType getKey() {
        return key;
    }

    public void setKey(KeyType key) {
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}