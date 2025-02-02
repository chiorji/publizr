package dev.publisher.post;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    private final JdbcClient jdbcClient;

    public PostRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    void save(Post post) {
        var created = jdbcClient.sql("INSERT INTO posts (title, content, author_id) VALUES (?, ?, ?)")
                .params(List.of(post.title(), post.content(), post.author_id()))
                .update();
        Assert.state(created == 1, "failed to create post " + post.title());
    }

    List<Post> findAll() {
        return jdbcClient.sql("SELECT * FROM posts").query(Post.class).list();
    }

    Optional<Post> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM posts WHERE id = :id")
                .param("id", id)
                .query(Post.class)
                .optional();
    }
}
