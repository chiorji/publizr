package dev.publizr.user;

import dev.publizr.config.JWTService;
import dev.publizr.models.APIResponseDTO;
import dev.publizr.user.models.LoginDTO;
import dev.publizr.user.models.SignUpDTO;
import dev.publizr.user.models.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
@CacheEvict(value = "users", allEntries = true)
@Tag(name = "User APIs", description = "Create, Read, Update, Delete")
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);
	private final UserRepository userRepository;
	private final JWTService jwtService;

	public UserController(UserRepository userRepository, JWTService jwtService) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}


	@GetMapping("/list")
	@Operation(
		summary = "Get the list of users",
		description = "This endpoint returns the list of all users signed up on the platform",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Users retrieved successfully",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve user list",
				content = @Content
			)
		}
	)

	ResponseEntity<APIResponseDTO<List<UserDTO>>> list() {
		try {
			List<UserDTO> userDTO = userRepository.list();
			APIResponseDTO<List<UserDTO>> responseDTO = new APIResponseDTO<>(true, "Users retrieved", userDTO, userDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Fetching user list failed -- '{}'", e.getLocalizedMessage());
			APIResponseDTO<List<UserDTO>> errorResponse = new APIResponseDTO<>(false, "Error occurred while retrieving users", null, 0);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping("/login")
	@Operation(
		summary = "User login",
		description = "A registered user can sign in to publish",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Login successful",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Login failed",
				content = @Content
			)
		}
	)

	ResponseEntity<APIResponseDTO<Map<String, Object>>> findById(@Valid @RequestBody LoginDTO loginDTO) {
		Map<String, Object> dataMap = new HashMap<>();
		try {
			UserDTO validatedDTO = userRepository.validate(loginDTO);
			String jwtToken = jwtService.generateJWTToken(validatedDTO);
			dataMap.put("token", jwtToken);
			dataMap.put("data", validatedDTO);
			APIResponseDTO<Map<String, Object>> responseDTO = new APIResponseDTO<>(true, "Users retrieved", dataMap, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Login failed -- '{}'", e.getLocalizedMessage());
			APIResponseDTO<Map<String, Object>> responseDTO = new APIResponseDTO<>(false, "Invalid email and password combination", null, null);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping("/signup")
	@Operation(
		summary = "User signup",
		description = "Onboards a new user to the platform, after which they have author's privilege to publish",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sign up successful",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Sign up failed",
				content = @Content
			)
		}
	)
	ResponseEntity<APIResponseDTO<Map<String, Object>>> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {

		try {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
			if (!pattern.matcher(signUpDTO.email()).matches())
				throw new RuntimeException(String.format("Email '%s' is invalid", signUpDTO.email()));

			long emailCount = userRepository.findByEmail(signUpDTO.email());

			if (emailCount > 0) {
				log.error("User with email address '{}' already exist", signUpDTO.email());
				APIResponseDTO<Map<String, Object>> responseDTO = new APIResponseDTO<>(false, String.format("User with email address '%s' already exist", signUpDTO.email()), null, 0);
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}

			Integer createdUserId = userRepository.createUser(signUpDTO);
			UserDTO userDTO = userRepository.findByUserId(createdUserId);
			String jwtToken = jwtService.generateJWTToken(userDTO);

			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("token", jwtToken);
			dataMap.put("data", userDTO);

			APIResponseDTO<Map<String, Object>> responseDTO = new APIResponseDTO<>(true, "Users retrieved", dataMap, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

		} catch (RuntimeException e) {
			APIResponseDTO<Map<String, Object>> responseDTO = new APIResponseDTO<>(false, "Sign up failed, but don't fret - you can retry.", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}
}
