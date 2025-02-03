package dev.publisher.user;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(User user) {
        var created = jdbcClient.sql("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")
                .params(List.of(user.username(), user.email(), user.password()))
                .update();
        Assert.state(created == 1, "failed to create user " + user.username());
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list();
    }

    public Optional<User> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM users WHERE id = :id")
                .param("id", id)
                .query(User.class)
                .optional();
    }
}
