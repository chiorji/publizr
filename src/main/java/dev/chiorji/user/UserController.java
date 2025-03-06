package dev.chiorji.user;

import dev.chiorji.models.*;
import dev.chiorji.user.models.*;
import io.swagger.v3.oas.annotations.*;
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
@RequestMapping("/api/users")
@Tag(name = "User APIs", description = "Create, Read, Update, Delete")
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}


	@GetMapping("")
	@Operation(
		summary = "Get the list of users",
		description = "This endpoint returns the list of all users signed up on the platform",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Users retrieved successfully",
				content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retrieve user list",
				content = @Content
			)
		},
		security = @SecurityRequirement(name = "Bearer Auth")
	)
	ResponseEntity<ResponseDTO<List<UserDTO>>> getAllUsers() {
		try {
			List<UserDTO> userDTO = userService.getAllUsers();
			ResponseDTO<List<UserDTO>> responseDTO = new ResponseDTO<>(true, "Users retrieved", userDTO, userDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			log.error("Fetching user list failed -- '{}'", e.getLocalizedMessage());
			ResponseDTO<List<UserDTO>> errorResponse = new ResponseDTO<>(false, "Error occurred while retrieving users", null, 0);
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
				content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Login failed",
				content = @Content
			)
		}
	)
	ResponseEntity<ResponseDTO<LoginUpResponseDTO>> processLoginRequest(@Valid @RequestBody LoginDTO loginDTO) {
		try {
			LoginUpResponseDTO data = userService.processLoginRequest(loginDTO);
			ResponseDTO<LoginUpResponseDTO> responseDTO = new ResponseDTO<>(true, "Login successful", data, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Login failed -- '{}'", e.getLocalizedMessage());
			ResponseDTO<LoginUpResponseDTO> responseDTO = new ResponseDTO<>(false, e.getMessage(), null, null);
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
				content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Sign up failed",
				content = @Content
			)
		}
	)
	ResponseEntity<ResponseDTO<LoginUpResponseDTO>> processSignUpRequest(@Valid @RequestBody SignUpDTO signUpDTO) {
		try {
			LoginUpResponseDTO data = userService.processSignUpRequest(signUpDTO);
			ResponseDTO<LoginUpResponseDTO> responseDTO = new ResponseDTO<>(true, "Sign up successful", data, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			ResponseDTO<LoginUpResponseDTO> responseDTO = new ResponseDTO<>(false, "Sign up failed, but don't fret - retry with another email.", null, 0);
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}
}
