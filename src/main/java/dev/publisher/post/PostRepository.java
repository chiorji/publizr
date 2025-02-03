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
        var created = jdbcClient.sql("INSERT INTO posts (title, excerpt, content, author_id, category, image_url, featured, tags, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .params(List.of(post.title(), post.excerpt(), post.content(), post.author_id(), post.category(), post.image_url(), post.featured(), post.tags(), post.status()))
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
