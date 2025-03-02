package dev.chiorji.config;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.*;
import dev.chiorji.user.models.*;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class JWTService {
	@Value("${secret.key}")
	String secretKey;

	@Value("${secret.token.issuer}")
	String tokenIssuer;


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
