package dev.chiorji.category;

import dev.chiorji.models.*;
import java.util.*;
import org.apache.logging.log4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private static final Logger log = LogManager.getLogger(CategoryController.class);
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("")
	ResponseEntity<APIResponseDTO<List<Category>>> getAllCategories() {
		try {
			List<Category> categories = categoryService.getAllCategories();
			APIResponseDTO<List<Category>> responseDTO = new APIResponseDTO<>(true, "Categories retrieved", categories, categories.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Failed to fetch a post for the specified author -- '{}'", e.getMessage());
			APIResponseDTO<List<Category>> responseDTO = new APIResponseDTO<>(false, "Failed to retrieve categories", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}
}
