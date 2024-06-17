package ch.ffhs.jea.auth;

public class AuthResponse {
    private final String token;
    private final NamedEntityReference<Long> user;

    public AuthResponse(String token, NamedEntityReference<Long> user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public NamedEntityReference<Long> getUser() {
        return user;
    }
}
