package dev.chiorji.category;

import java.util.*;
import org.springframework.stereotype.*;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public void addCategory(String name) {
		categoryRepository.addCategory(name);
	}

	public List<Category> getAllCategories() {
		return categoryRepository.getAllCategory();
	}
}
