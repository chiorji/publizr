package dev.publizr.user;

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
        var created = jdbcClient.sql("INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)")
                .params(List.of(user.username(), user.email(), user.password()))
                .update();
        Assert.state(created == 1, "failed to create user " + user.username());
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
}
