package dev.chiorji.category;

import java.util.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Transactional
@Repository
public class CategoryRepository {
	private final JdbcClient jdbcClient;

	public CategoryRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public void addCategory(String name) {
		jdbcClient.sql("INSERT INTO CATEGORIES (NAME) VALUES (?)").param("NAME", name).update();
	}

	public Category getCategory(Integer id) {
		return jdbcClient.sql("SELECT * FROM CATEGORIES WHERE ID = :ID").param("ID", id).query(Category.class).single();
	}

	public List<Category> getAllCategory() {
		return jdbcClient.sql("SELECT * FROM CATEGORIES").query(Category.class).list();
	}
}
