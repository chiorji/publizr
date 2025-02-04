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
        var created = jdbcClient.sql("INSERT INTO POSTS (title, excerpt, content, author_id, category, poster_card, featured, tags, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .params(List.of(post.title(), post.excerpt(), post.content(), post.author_id(), post.category(), post.poster_card(), post.featured(), post.tags(), post.status()))
                .update();
        Assert.state(created == 1, "failed to create post " + post.title());
    }

    List<PostAuthorData> findAll() {
        return jdbcClient.sql("SELECT * FROM POSTS INNER JOIN USERS ON POSTS.STATUS ILIKE '%PUBLISHED' ORDER BY POSTS.ID").query(PostAuthorData.class).list();
    }

    Optional<Post> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM POSTS WHERE id = :id")
                .param("id", id)
                .query(Post.class)
                .optional();
    }

    List<PostAuthorData> findAllByAuthorId(Integer id) {
        return jdbcClient.sql("SELECT (USERS.ID) AS AUTHOR_ID, CATEGORY, CONTENT, POSTED_ON, " +
                "EXCERPT, FEATURED, POSTER_CARD, (POSTS.ID) AS POST_ID,\n" +
                "STATUS, TAGS, TITLE, LAST_UPDATED, USERNAME \n" +
                "FROM POSTS \n" +
                "INNER JOIN USERS \n" +
                "ON POSTS.STATUS ILIKE '%PUBLISHED' \n" +
                "AND USERS.ID = :id\n" +
                "ORDER BY POSTS.ID").param("id", id).query(PostAuthorData.class).stream().toList();
    }
}
