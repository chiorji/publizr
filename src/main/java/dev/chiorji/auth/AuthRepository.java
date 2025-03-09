package dev.chiorji.auth;

import java.util.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

@Repository
public class AuthRepository {
	private final JdbcClient jdbcClient;

	public AuthRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public KeyHolder createUser(SignUpDTO user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)")
			.params(List.of(user.username(), user.email(), user.password()))
			.update(keyHolder);
		return keyHolder;
	}

	public Boolean updatePassword(LoginDTO loginDTO) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("UPDATE USERS SET PASSWORD = ? WHERE EMAIL = ?")
			.params(List.of(loginDTO.password(), loginDTO.email()))
			.update(keyHolder);
		return Objects.requireNonNull(keyHolder.getKeys()).isEmpty();
	}

	public Integer providedEmailExists(final String email) {
		return jdbcClient.sql("SELECT COUNT(*) FROM USERS WHERE EMAIL = :EMAIL")
			.param("EMAIL", email).query(Integer.class).single();
	}
}
