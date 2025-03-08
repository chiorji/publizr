package dev.chiorji.user;

import com.auth0.jwt.interfaces.*;
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
	ResponseEntity<ResponseDTO<List<UserDTO>>> getAllUsers(@RequestAttribute("claims") Map<Object, Claim> claim) {
		log.info("Extracted claims --> {} <-- ", claim);
		try {
			String role = String.valueOf(claim.get("role"));
			String email = String.valueOf(claim.get("email"));
			List<UserDTO> userDTO = userService.getAllActiveAndInActiveUsers(role, email);
			log.info("user requested with an -->  {} <-- role", role);
			ResponseDTO<List<UserDTO>> responseDTO = new ResponseDTO<>(true, "Users retrieved", userDTO, userDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			e.printStackTrace();
			log.error("Fetching user list failed -- '{}'", e.getLocalizedMessage());
			ResponseDTO<List<UserDTO>> errorResponse = new ResponseDTO<>(false, "Error occurred while retrieving users", null, 0);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> processUserSoftDelete(@PathVariable @Valid Integer id) {
		Boolean softDeleted = userService.softDeleteUserById(id);
		return new ResponseEntity<>(softDeleted, HttpStatus.OK);
	}
}
