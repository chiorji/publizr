package dev.publizr.post;

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
        var created = jdbcClient.sql("""
                        INSERT INTO POSTS (TITLE, EXCERPT, CONTENT, AUTHOR_ID, CATEGORY, POSTER_CARD, FEATURED, TAGS, STATUS)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .params(List.of(post.title(), post.excerpt(), post.content(), post.author_id(), post.category(),
                        post.poster_card(), post.featured(), post.tags(), post.status()))
                .update();
        Assert.state(created == 1, "failed to create post " + post.title());
    }

    List<PostDTO> findAll() {
        return jdbcClient.sql("""
                SELECT DISTINCT
                P.AUTHOR_ID,
                P.CATEGORY,
                P.CONTENT,
                P.POSTED_ON,
                P.EXCERPT,
                P.FEATURED,
                P.POSTER_CARD,
                (P.ID) AS POST_ID,
                P.STATUS,
                P.TAGS,
                P.TITLE,
                P.LAST_UPDATED,
                A.USERNAME
                FROM POSTS P JOIN USERS A
                ON P.STATUS ILIKE '%PUBLISHED'
                ORDER BY P.POSTED_ON""").query(PostDTO.class).list();
    }

    Optional<PostDTO> findById(Integer id) {
        return jdbcClient.sql("""
                        SELECT
                        P.CATEGORY,
                        P.CONTENT,
                        P.POSTED_ON,
                        P.EXCERPT,
                        P.FEATURED,
                        P.POSTER_CARD,
                        (P.ID) AS POST_ID,
                        P.STATUS,
                        P.TAGS,
                        P.TITLE,
                        P.LAST_UPDATED,
                        P.AUTHOR_ID,
                        A.USERNAME
                        FROM POSTS P JOIN USERS A
                        ON P.AUTHOR_ID = A.ID
                        WHERE P.STATUS ILIKE '%PUBLISHED'
                        AND P.ID = :ID
                        """)
                .param("ID", id)
                .query(PostDTO.class)
                .optional();
    }

    List<PostDTO> findAllByAuthorId(Integer id) {
        return jdbcClient.sql("""
                SELECT
                P.AUTHOR_ID,
                P.CATEGORY,
                P.CONTENT,
                P.POSTED_ON,
                P.EXCERPT,
                P.FEATURED,
                P.POSTER_CARD,
                (P.ID) AS POST_ID,
                P.STATUS,
                P.TAGS,
                P.TITLE,
                P.LAST_UPDATED,
                A.USERNAME
                FROM POSTS P JOIN USERS A
                ON P.AUTHOR_ID = :ID
                AND P.STATUS ILIKE '%PUBLISHED'
                ORDER BY P.POSTED_ON""").param("ID", id).query(PostDTO.class).stream().toList();
    }

    public List<PostDTO> findOverview() {
        return jdbcClient.sql("""
                SELECT
                     P.AUTHOR_ID,
                     P.CATEGORY,
                     P.CONTENT,
                     P.POSTED_ON,
                     P.EXCERPT,
                     P.FEATURED,
                     P.POSTER_CARD,
                     (P.ID) AS POST_ID,
                     P.STATUS,
                     P.TAGS,
                     P.TITLE,
                     P.LAST_UPDATED,
                     A.USERNAME
                     FROM POSTS P INNER JOIN USERS A
                     ON P.AUTHOR_ID = A.ID
                     AND P.STATUS ILIKE '%PUBLISHED'
                     ORDER BY P.POSTED_ON
                     LIMIT 10
                """).query(PostDTO.class).stream().toList();
    }
}
