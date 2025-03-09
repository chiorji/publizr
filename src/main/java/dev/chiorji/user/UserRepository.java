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

	public List<UserDTO> getAllActiveUsers() {
		return jdbcClient.sql(
			"""
				SELECT * FROM USERS
				WHERE ROLE = 'AUTHOR'
				AND IS_DELETED = FALSE
				ORDER BY CREATED_AT DESC
				"""
		).query(UserDTO.class).list();
	}

	public List<UserDTO> getActiveAndInActiveUsers() {
		return jdbcClient.sql("SELECT * FROM USERS ORDER BY CREATED_AT DESC").query(UserDTO.class).list();
	}

	public User findUserByEmail(String email) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = ?").params(List.of(email)).query(User.class).single();
	}

	public UserDTO findByUserId(Integer id) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE ID = :ID")
			.param("ID", id).query(UserDTO.class).single();
	}

	public Integer totalEntries() {
		return jdbcClient.sql("SELECT COUNT(*) FROM USERS").query(Integer.class).single();
	}

	public Boolean softDeleteUserById(Integer id) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("UPDATE USERS SET IS_DELETED = TRUE WHERE ID = ? AND ROLE != 'ADMIN'")
			.params(List.of(id))
			.update(keyHolder);
		return !Objects.requireNonNull(keyHolder.getKeys()).isEmpty();
	}

	public User findUserByEmailAndRole(String email, String role) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = ? AND ROLE = ?")
			.params(List.of(email, role))
			.query(User.class)
			.single();
	}
}