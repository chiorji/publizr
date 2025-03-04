package dev.chiorji.image;

import java.util.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Transactional
@Repository
public class ImageRepository {
	private final JdbcClient jdbcClient;

	public ImageRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public Image saveImage(ImageDTO image) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("INSERT INTO IMAGES(NAME, URL) VALUES(? , ?) RETURNING *")
			.params(List.of(image.name(), image.url()))
			.update(keyHolder);
		return new Image((Integer) keyHolder.getKeys().get("ID"), (String) keyHolder.getKeys().get("NAME"), (String) keyHolder.getKeys().get("URL"));
	}

	public Image getImage(Integer id) {
		return jdbcClient.sql("SELECT * FROM IMAGES WHERE ID = :ID").param("ID", id).query(Image.class).single();
	}

	public void deleteImage(Integer id) {
		jdbcClient.sql("DELETE FROM IMAGES WHERE ID = :ID").param("ID", id).update();
	}
}
