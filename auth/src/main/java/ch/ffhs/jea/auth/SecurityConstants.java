package ch.ffhs.jea.auth;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class SecurityConstants {

    public static final String SECRET = "Secret Key to generate JWT's (min 256 Bits)";

    public static final String ALGORITHM = "HmacSHA256";

    public static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), ALGORITHM);

    public static final long EXPIRATION_TIME = 864_000_000L; // 10 days
    public static final long EXPIRATION_TIME_AUTH = 3_600_000L; // 1 hour

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

}
