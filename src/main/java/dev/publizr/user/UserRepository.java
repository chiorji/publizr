package dev.publizr.user;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {
	private final JdbcClient jdbcClient;

	public UserRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public Integer createUser(User user) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var created = jdbcClient.sql("INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)")
				.params(List.of(user.username(), user.email(), user.password()))
				.update(keyHolder);
			Assert.state(created == 1, "failed to create user " + user.username());
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("USER_ID");
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public List<UserDTO> findAll() {
		return jdbcClient.sql("SELECT * FROM USERS").query(UserDTO.class).list();
	}

	public Optional<UserDTO> findById(Integer id) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE ID = :ID")
			.param("ID", id)
			.query(UserDTO.class)
			.optional();
	}

	public Integer findUserByEmailAddress(final String email) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = :EMAIL")
			.param("EMAIL", email).query(User.class).list().size();
	}

	public User findByUserId(Integer id) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE USER_ID = :USER_ID")
			.param("USER_ID", id).query(User.class).single();
	}
}
