package dev.publizr.user;

import dev.publizr.user.models.LoginDTO;
import dev.publizr.user.models.SignUpDTO;
import dev.publizr.user.models.User;
import dev.publizr.user.models.UserDTO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class UserRepository {
	private final JdbcClient jdbcClient;

	public UserRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public void saveAll(List<SignUpDTO> signUpDTO) {
		signUpDTO.forEach(this::createUser);
	}

	public Integer createUser(SignUpDTO user) {
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

	public List<UserDTO> list() {
		return jdbcClient.sql("SELECT * FROM USERS").query(UserDTO.class).list();
	}

	public UserDTO validate(LoginDTO payload) {
		try {
			User user = findUserByEmail(payload.email());
			if (!BCrypt.checkpw(payload.password(), user.password())) {
				throw new RuntimeException("Hey, There's a catch! - An invalid email/password combination");
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
			throw new RuntimeException(e.getMessage());
		}
	}

	private User findUserByEmail(String email) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE EMAIL = :EMAIL").param("EMAIL", email).query(User.class).single();
	}

	public Integer providedEmailExists(final String email) {
		var count = jdbcClient.sql("SELECT COUNT(*) FROM USERS WHERE EMAIL = :EMAIL")
			.param("EMAIL", email).query(Integer.class).single();
		Assert.isTrue(count == 0, "Provided email already exist");
		return count;
	}

	public UserDTO findByUserId(Integer id) {
		return jdbcClient.sql("SELECT * FROM USERS WHERE ID = :ID")
			.param("ID", id).query(UserDTO.class).single();
	}

	public Integer totalEntries() {
		return jdbcClient.sql("SELECT COUNT(*) FROM USERS").query(Integer.class).single();
	}
}
