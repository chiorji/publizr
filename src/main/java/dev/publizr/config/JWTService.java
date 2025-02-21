package dev.publizr.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import dev.publizr.user.models.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {
	@Value("${secret.key}")
	private String secretKey;

	@Value("${secret.token.issuer}")
	private String tokenIssuer;


	public String generateJWTToken(UserDTO user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secretKey);
			long expireString = Long.parseLong(String.valueOf(3));
			LocalDateTime expires = LocalDateTime.now().plusHours(expireString);
			Instant instant = expires.atZone(ZoneId.systemDefault()).toInstant();

			return JWT.create()
				.withIssuer(tokenIssuer)
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
