package dev.chiorji.user;

import dev.chiorji.execption.*;
import dev.chiorji.models.*;
import dev.chiorji.user.models.*;
import dev.chiorji.util.*;
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
	private final Constant constant;

	public UserController(UserService userService, Constant constant) {
		this.userService = userService;
		this.constant = constant;
	}


	@GetMapping("/list")
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
	public ResponseEntity<ResponseDTO<List<UserDTO>>> getAllUsers() {
		try {
			List<UserDTO> userDTO = userService.getAllUsers();
			ResponseDTO<List<UserDTO>> responseDTO = new ResponseDTO<>(true, "Users retrieved", userDTO, userDTO.size());
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while retrieving users", e);
			throw new AuthenticationFailedException("Error occurred while retrieving users");
		}
	}


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> processUserSoftDelete(@PathVariable @Valid Integer id) {
		try {
//			RoleInfo roleInfo = constant.getRole(claims);
			Boolean softDeleted = userService.softDeleteUserById(id);
			return new ResponseEntity<>(softDeleted, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e);
			throw new User404Exception(e.getMessage());
		}
	}
}
