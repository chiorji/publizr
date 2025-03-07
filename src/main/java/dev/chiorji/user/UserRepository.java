package dev.chiorji.user;

import dev.chiorji.user.models.*;
import java.util.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Transactional
@Repository
public class UserRepository {
	private final JdbcClient jdbcClient;

	public UserRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public KeyHolder createUser(SignUpDTO user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)")
			.params(List.of(user.username(), user.email(), user.password()))
				.update(keyHolder);
		return keyHolder;
	}

	public List<UserDTO> getAllUsers() {
		return jdbcClient.sql("SELECT * FROM USERS").query(UserDTO.class).list();
	}

	public User findUserByEmail(String email) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = :EMAIL").param("EMAIL", email).query(User.class).single();
	}

	public Integer providedEmailExists(final String email) {
		return jdbcClient.sql("SELECT COUNT(*) FROM USERS WHERE EMAIL = :EMAIL")
			.param("EMAIL", email).query(Integer.class).single();
	}

	public UserDTO findByUserId(Integer id) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE ID = :ID")
			.param("ID", id).query(UserDTO.class).single();
	}

	public Integer totalEntries() {
		return jdbcClient.sql("SELECT COUNT(*) FROM USERS").query(Integer.class).single();
	}

	public void updatePassword(LoginDTO loginDTO) {
		jdbcClient.sql("UPDATE USERS SET PASSWORD = ? WHERE EMAIL = ?")
			.params(List.of(loginDTO.password(), loginDTO.email()))
			.update();
	}

	public void softDeleteUserById(Integer id) {
		jdbcClient.sql("UPDATE USERS SET IS_DELETED = TRUE WHERE ID = ? AND ROLE != 'ADMIN'")
			.params(List.of(id))
			.update();
	}
}