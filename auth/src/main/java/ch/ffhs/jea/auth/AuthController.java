package ch.ffhs.jea.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Date;

@Path("auth")
public class AuthController {
    private final UserService userService;

    @Inject
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("login")
    public Response authenticate(AuthRequest authRequest) {
        try {
            if (userService.checkPassword(authRequest.getUsername(), authRequest.getPassword())) {
                User user = userService.getUserByUsername(authRequest.getUsername());
                return Response.ok(new AuthResponse(generateJwtToken(user), new NamedEntityReference<>(user.getId(), user.getUsername()))).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("register")
    public Response register(RegisterRequest registerRequest) {
        try {
            User user = userService.createUser(new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail()));

            return Response.status(Response.Status.CREATED).entity(new AuthResponse(generateJwtToken(user), new NamedEntityReference<>(user.getId(), user.getUsername()))).build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    private String generateJwtToken(User user) {
        try {
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), buildJWTClaimsSet(user));
            jwt.sign(new MACSigner(SecurityConstants.SECRET_KEY_SPEC));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private JWTClaimsSet buildJWTClaimsSet(User user) {
        return new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .expirationTime(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .build();
    }
}
