package dev.chiorji.auth;

import dev.chiorji.execption.*;
import dev.chiorji.models.*;
import dev.chiorji.user.models.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.*;
import org.apache.logging.log4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger log = LogManager.getLogger(AuthController.class);
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
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
			LoginUpResponseDTO data = authService.processLoginRequest(loginDTO);
			ResponseDTO<LoginUpResponseDTO> responseDTO = new ResponseDTO<>(true, "Login successful", data, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			throw new AuthenticationFailedException(e.getMessage());
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
			LoginUpResponseDTO data = authService.processSignUpRequest(signUpDTO);
			ResponseDTO<LoginUpResponseDTO> responseDTO = new ResponseDTO<>(true, "Sign up successful", data, 1);
			return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			throw new AuthenticationFailedException(e.getMessage());
		}
	}

	@PutMapping("/reset-password")
	public ResponseEntity<Boolean> processResetPassword(@RequestBody @Valid LoginDTO loginDTO) {
		try {
			Boolean resetComplete = authService.processPasswordReset(loginDTO);
			return new ResponseEntity<>(resetComplete, HttpStatus.OK);
		} catch (Exception e) {
			throw new AuthenticationFailedException(e.getMessage());
		}
	}
}
