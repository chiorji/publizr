package dev.chiorji.post;

import dev.chiorji.models.APIResponseDTO;
import dev.chiorji.post.models.Post;
import dev.chiorji.post.models.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post APIs", description = "Create, Read, Update, and Delete publications")
public class PostController {
	private static final Logger log = LogManager.getLogger(PostController.class);

	private final PostRepository postRepository;

	public PostController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("")
	@Operation(
		summary = "Get the list of publications",
		description = "This endpoint returns the list of all the publication on the platform",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publications retrieved",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publications",
				content = @Content
			)
		}
	)
	ResponseEntity<APIResponseDTO<List<PostDTO>>> list() {
		try {
			List<PostDTO> postDTO = postRepository.list();
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(true, "publications retrieved", postDTO, postDTO.size());
			return ResponseEntity.ok(responseDTO);
		} catch (RuntimeException e) {
			log.error("Fetching post list failed -- '{}'", e.getMessage());
			APIResponseDTO<List<PostDTO>> errorResponse = new APIResponseDTO<>(false, "Error occurred while retrieving publications", null, 0);
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
				description = "Publication retrieved",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		}
	)
	@Parameter(name = "ID", description = "Post ID", required = true, in = ParameterIn.PATH)
	ResponseEntity<APIResponseDTO<PostDTO>> findById(@PathVariable Integer id) {
		try {
			PostDTO postDTO = postRepository.findPostById(id);
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(true, "publication retrieved", postDTO, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Fetching post failed -- '{}'", e.getMessage());
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(false, "Could not retrieve information for post with ID " + id, null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/publish")
	@Operation(
		summary = "Publish a post",
		description = "Publishes a post",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication successful",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to publish",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	ResponseEntity<APIResponseDTO<PostDTO>> create(@RequestBody @Valid Post post) {
		try {
			Integer postId = postRepository.save(post);
			PostDTO newPost = postRepository.findPostById(postId);
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(true, "published successfully", newPost, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			log.error("Publishing a post failed -- '{}'", e.getMessage());
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(false, "An error occurred while publishing post", null, 0);
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
				description = "Publication retrieved",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	@Parameter(in = ParameterIn.PATH, name = "id", description = "author's id")
	ResponseEntity<APIResponseDTO<List<PostDTO>>> byAuthorId(@PathVariable Integer id) {
		try {
			List<PostDTO> postDTO = postRepository.byAuthorId(id);
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(true, "publications retrieved successfully", postDTO, postDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Failed to fetch a post for the specified author -- '{}'", e.getMessage());
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(false, "Post does not exist", null, 0);
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
				description = "Publication retrieved",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		}
	)
	ResponseEntity<APIResponseDTO<List<PostDTO>>> recent() {
		List<PostDTO> postDTO = null;
		try {
			Integer count = postRepository.totalEntries();
			if (count < 6) postDTO = postRepository.list();
			else postDTO = postRepository.recent();
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(true, "Posts retrieved successfully", postDTO, count);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Fetching recent posts failed -- '{}'", e.getMessage());
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(false, "Error retrieving posts", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	@Operation(
		summary = "Delete a post",
		description = "Delete a post associated to the ID provided as parameter",
		responses = {
			@ApiResponse(
				responseCode = "204",
				description = "Publication deleted successfully",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to delete publication",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	@Parameter(in = ParameterIn.PATH, name = "id", description = "Id of the post to be deleted", required = true)
	ResponseEntity<APIResponseDTO<Void>> deleteById(@PathVariable @Valid Integer id) {
		try {
			Integer deletedPostId = postRepository.deleteById(id);
			if (deletedPostId < 0) throw new RuntimeException("Failed! Post was not deleted due to error.");
			APIResponseDTO<Void> responseDTO = new APIResponseDTO<>(true, "Publication deleted successfully", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			log.error("Deleting a post failed -- '{}'", e.getMessage());
			APIResponseDTO<Void> responseDTO = new APIResponseDTO<>(false, "Failed to delete post", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}


	@PutMapping("/update/{id}")
	@Operation(
		summary = "Update a post",
		description = "Update a post associated to the ID provided as parameter",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publication retrieved",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve publication",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	@Parameter(in = ParameterIn.PATH, name = "id", description = "Id of the post to be updated", required = true)
	ResponseEntity<APIResponseDTO<PostDTO>> update(@RequestBody @Valid PostDTO postDTO) {
		try {
			Integer postId = postRepository.update(postDTO);
			PostDTO postDTO1 = postRepository.findPostById(postId);
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(true, "Post updated successfully", postDTO1, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			log.error("Updating a post failed -- '{}'", e.getMessage());
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(false, "Error: could not update post, please try again", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}
}
