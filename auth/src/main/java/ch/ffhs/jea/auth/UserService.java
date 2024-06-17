package ch.ffhs.jea.auth;

public interface UserService {
    User createUser(User user)  throws UserCreationFailedException;
    boolean checkPassword(String username, String password);
    User getUserByUsername(String username);
}
