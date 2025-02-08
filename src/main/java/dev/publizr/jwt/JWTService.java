package dev.publizr.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import dev.publizr.user.UserDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTService {
	private final String SECRET_KEY = "APISECRETRANDOMKEY";
	private final String TOKEN_ISSUER = "publizr";

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

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public String validateJWTToken(String jwtToken) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.require(algorithm)
				.withIssuer(TOKEN_ISSUER)
				.build()
				.verify(jwtToken)
				.getToken();
		} catch (JWTDecodeException e) {
			throw new RuntimeException(e);
		}
	}
}
