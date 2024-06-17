package ch.ffhs.jea.auth;

public class UserCreationFailedException extends Exception {
    public UserCreationFailedException(String message) {
        super(message);
    }
}
