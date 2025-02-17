package dev.publizr.post;

import dev.publizr.models.APIResponseDTO;
import dev.publizr.post.models.Post;
import dev.publizr.post.models.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CacheEvict(value = "posts", allEntries = true)
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
			e.printStackTrace();
			log.error("Fetching post list failed -- '{}'", e.getLocalizedMessage());
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
	ResponseEntity<APIResponseDTO<PostDTO>> findById(@PathVariable @Parameter(name = "ID", description = "Post ID") Integer id) {
		try {
			PostDTO postDTO = postRepository.findById(id);
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(true, "publication retrieved", postDTO, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Fetching post failed -- '{}'", e.getLocalizedMessage());
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
		}
	)
	@Parameter(in = ParameterIn.HEADER, name = "Authorization", schema = @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "token"))
	ResponseEntity<APIResponseDTO<PostDTO>> create(@RequestBody @Valid Post post) {
		try {
			Integer postId = postRepository.save(post);
			PostDTO newPost = postRepository.findById(postId);
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(true, "published successfully", newPost, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			log.error("Publishing a post failed -- '{}'", e.getLocalizedMessage());
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
		}
	)
	@Parameter(in = ParameterIn.HEADER, name = "Authorization", schema = @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED))
	ResponseEntity<APIResponseDTO<List<PostDTO>>> byAuthorId(@PathVariable @Parameter(name = "id", description = "author's id") Integer id) {
		try {
			List<PostDTO> postDTO = postRepository.byAuthorId(id);
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(true, "published successfully", postDTO, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			log.error("Failed to fetch a post for the specified author -- '{}'", e.getLocalizedMessage());
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
		try {
			List<PostDTO> postDTO = postRepository.recent();
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(true, "published successfully", postDTO, postDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			log.error("Fetching recent posts failed -- '{}'", e.getLocalizedMessage());
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(false, "Error retrieving posts", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(
		summary = "Delete a post",
		description = "Delete a post associated to the ID provided as parameter",
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
	ResponseEntity<APIResponseDTO<Void>> deleteById(@PathVariable @Parameter(name = "id", description = "Id of the post to be deleted") Integer id) {
		try {
			Integer deletedPostId = postRepository.deleteById(id);
			if (deletedPostId < 0) throw new RuntimeException("Failed! Post was not deleted due to error.");
			APIResponseDTO<Void> responseDTO = new APIResponseDTO<>(true, "Post deleted successfully", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			log.error("Deleting a post failed -- '{}'", e.getLocalizedMessage());
			APIResponseDTO<Void> responseDTO = new APIResponseDTO<>(false, "Failed to delete post", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}


	@PutMapping("/{id}")
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
		}
	)
	ResponseEntity<APIResponseDTO<PostDTO>> update(@RequestBody @Valid @Parameter(name = "id", description = "Id of the post to be updated") PostDTO postDTO) {
		try {
			PostDTO postDTO1 = postRepository.update(postDTO);
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(true, "Post updated successfully", postDTO1, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			e.printStackTrace((PrintStream) log);
			log.error("Updating a post failed -- '{}'", e.getLocalizedMessage());
			APIResponseDTO<PostDTO> responseDTO = new APIResponseDTO<>(false, "Error: could not update post, please try again", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}
}
