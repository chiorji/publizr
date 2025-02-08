package dev.publizr.user;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository {
	private final JdbcClient jdbcClient;

	public UserRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public Integer createUser(UserSignUpPayload user) {
		try {
			String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt(10));
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var created = jdbcClient.sql("INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)")
				.params(List.of(user.username(), user.email(), hashedPassword))
				.update(keyHolder);
			Assert.state(created == 1, "failed to create user " + user.username());
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public List<UserDTO> findAll() {
		return jdbcClient.sql("SELECT * FROM USERS").query(UserDTO.class).list();
	}

	public UserDTO findByEmailAndPassword(LoginPayload payload) {
		try {
			var user = jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = :EMAIL")
				.param("EMAIL", payload.email())
				.query(User.class)
				.single();
			System.out.println(user);
			if (!BCrypt.checkpw(payload.password(), user.password())) {
				throw new RuntimeException("HEY! THERE'S A CATCH - EITHER AN INVALID EMAIL OR PASSWORD");
			}
			return new UserDTO(
				user.id(),
				user.username(),
				user.email(),
				user.role(),
				user.image_url(),
				user.created_at(),
				user.updated_at()
			);
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public Integer findUserByEmailAddress(final String email) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = :EMAIL")
			.param("EMAIL", email).query(User.class).list().size();
	}

	public UserDTO findByUserId(Integer id) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE ID = :ID")
			.param("ID", id).query(UserDTO.class).single();
	}
}
