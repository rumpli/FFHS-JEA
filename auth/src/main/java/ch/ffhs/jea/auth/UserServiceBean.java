package ch.ffhs.jea.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

@Stateless
public class UserServiceBean implements UserService {
    @PersistenceContext(unitName = "blog")
    private EntityManager em;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public User createUser(User user) throws UserCreationFailedException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.persist(user);
        em.flush();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/users"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + generateJwtToken())
                .POST(HttpRequest.BodyPublishers.ofString(user.toJsonString()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                throw new UserCreationFailedException("" + user.getId());
            }
        } catch (IOException | InterruptedException e) {
            throw new UserCreationFailedException(e.getMessage());
        }
        return user;
    }

    @Override
    public boolean checkPassword(String username, String password) {
        User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public User getUserByUsername(String username) {
        return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
    }


    private String generateJwtToken() {
        try {
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), buildJWTClaimsSet());
            jwt.sign(new MACSigner(SecurityConstants.SECRET_KEY_SPEC));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private JWTClaimsSet buildJWTClaimsSet() {
        return new JWTClaimsSet.Builder()
                .subject("AUTH_SERVER")
                .claim("scope", "AUTH_SERVER")
                .expirationTime(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_AUTH))
                .build();
    }
}
