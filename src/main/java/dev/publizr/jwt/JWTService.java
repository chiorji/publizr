package dev.publizr.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import dev.publizr.constants.Constants;
import dev.publizr.user.models.UserDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {
	Constants constants = new Constants();
	String TOKEN_ISSUER = constants.TOKEN_ISSUER;
	String SECRET_KEY = constants.SECRET_KEY;

	public String generateJWTToken(UserDTO user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			long expireString = Long.parseLong(String.valueOf(3));
			LocalDateTime expires = LocalDateTime.now().plusHours(expireString);
			Instant instant = expires.atZone(ZoneId.systemDefault()).toInstant();

			return JWT.create()
				.withIssuer(TOKEN_ISSUER)
				.withClaim("id", user.id())
				.withClaim("role", user.role())
				.withClaim("username", user.username())
				.withClaim("email", user.email())
				.withExpiresAt(Date.from(instant))
				.sign(algorithm);
		} catch (JWTCreationException e) {
			throw new RuntimeException(e);
		}
	}
}
