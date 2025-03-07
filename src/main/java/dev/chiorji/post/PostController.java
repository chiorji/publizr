package dev.chiorji.post;

import dev.chiorji.models.*;
import dev.chiorji.post.models.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import java.util.*;
import org.apache.logging.log4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/api/posts")
@Tag(name = "Post APIs", description = "Create, Read, Update, and Delete publications")
public class PostController {
	private static final Logger log = LogManager.getLogger(PostController.class);

	private final PostRepository postRepository;
	private final PostService postService;

	public PostController(PostRepository postRepository, PostService postService) {
		this.postRepository = postRepository;
		this.postService = postService;
	}

	@GetMapping("")
	@Operation(
		summary = "Get the list of publications",
		description = "This endpoint returns the list of all the publication on the platform",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publications retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publications",
				content = @Content
			)
		}
	)
	ResponseEntity<ResponseDTO<List<PostDTO>>> list() {
		try {
			List<PostDTO> postDTO = postService.list();
			ResponseDTO<List<PostDTO>> responseDTO = new ResponseDTO<>(true, "publications retrieved", postDTO, postDTO.size());
			return ResponseEntity.ok(responseDTO);
		} catch (RuntimeException e) {
			ResponseDTO<List<PostDTO>> errorResponse = new ResponseDTO<>(false, "Error occurred while retrieving publications", null, 0);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}

	
	@GetMapping("/{id}")
	@Operation(
		summary = "Post by ID",
		description = "This retrieves a single post with the specified ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		}
	)
	@Parameter(name = "ID", description = "Post ID", required = true, in = ParameterIn.PATH)
	ResponseEntity<ResponseDTO<PostDTO>> findById(@PathVariable Integer id) {
		try {
			PostDTO postDTO = postRepository.findPostById(id);
			ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>(true, "publication retrieved", postDTO, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Fetching post failed -- '{}'", e.getMessage());
			ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>(false, "Could not retrieve information for post with ID " + id, null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping(value = "/publish", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
		summary = "Publish a post",
		description = "Publishes a post",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication successful", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to publish",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	ResponseEntity<ResponseDTO<PostDTO>> publishPost(@ModelAttribute @Valid PublishDTO publishDTO) {
		try {
			PostDTO postDTO = postService.publishPost(publishDTO);
			ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>(true, "published successfully", postDTO, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>(false, "An error occurred while publishing post", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}


	@GetMapping("/author/{id}")
	@Operation(
		summary = "Post by author ID",
		description = "This retrieves posts published by an author",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	@Parameter(in = ParameterIn.PATH, name = "id", description = "author's id")
	ResponseEntity<ResponseDTO<List<PostDTO>>> getPostByAuthorId(@PathVariable Integer id) {
		try {
			List<PostDTO> postDTO = postService.getPostByAuthorId(id);
			ResponseDTO<List<PostDTO>> responseDTO = new ResponseDTO<>(true, "publications retrieved successfully", postDTO, postDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseDTO<List<PostDTO>> responseDTO = new ResponseDTO<>(false, "Post does not exist", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/recent")
	@Operation(
		summary = "Get top 10 recent post",
		description = "This retrieves top 10 most-recent posts published",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		}
	)
	ResponseEntity<ResponseDTO<List<PostDTO>>> getTenMostRecentPosts() {
		try {
			List<PostDTO> postDTO = postService.getTenMostRecentPosts();
			ResponseDTO<List<PostDTO>> responseDTO = new ResponseDTO<>(true, "Posts retrieved successfully", postDTO, postDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseDTO<List<PostDTO>> responseDTO = new ResponseDTO<>(false, "Error retrieving posts", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete")
	@Operation(
		summary = "Delete a post",
		description = "Delete a post associated to the ID provided as parameter",
		responses = {
			@ApiResponse(
				responseCode = "204",
				description = "Publication deleted successfully", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to delete publication",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	@Parameter(in = ParameterIn.PATH, name = "id", description = "Id of the post to be deleted", required = true)
	ResponseEntity<ResponseDTO<Void>> softDeletePostById(@ModelAttribute @Valid PostDeleteDTO postDeleteDTO) {
		try {
			postService.softDeletePostByIdAndAuthorId(postDeleteDTO);
			ResponseDTO<Void> responseDTO = new ResponseDTO<>(true, "Publication deleted successfully", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			ResponseDTO<Void> responseDTO = new ResponseDTO<>(false, "Failed to delete post", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/update/feature/{id}")
	public ResponseEntity<Boolean> updatePostFeatureStatus(@PathVariable @Valid Integer id) {
		Boolean deleted = postService.updatePostFeatureStatus(id);
		return new ResponseEntity<>(deleted, HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	@Operation(
		summary = "Update a post",
		description = "Update a post associated to the ID provided as parameter",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication retrieved", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	@Parameter(in = ParameterIn.PATH, name = "id", description = "Id of the post to be updated", required = true)
	ResponseEntity<ResponseDTO<PostDTO>> updatePost(@RequestBody @Valid PostUpdateDTO postUpdateDTO) {
		try {
			PostDTO postDTO = postService.updatePost(postUpdateDTO);
			ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>(true, "Post updated successfully", postDTO, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>(false, "Error: could not update post, please try again", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}
}