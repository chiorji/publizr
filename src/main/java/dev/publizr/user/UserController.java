package dev.publizr.user;

import dev.publizr.jwt.JWTService;
import dev.publizr.user.models.LoginDTO;
import dev.publizr.user.models.SignUpDTO;
import dev.publizr.user.models.UserDTO;
import jakarta.validation.Valid;
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
public class UserController {
	private final UserRepository userRepository;
	private final JWTService jwtService;

	public UserController(UserRepository userRepository, JWTService jwtService) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	@GetMapping("/list")
	ResponseEntity<Map<String, Object>> list() {
		Map<String, Object> response = new HashMap<>();
		List<UserDTO> userDTO = userRepository.list();

		response.put("success", true);
		response.put("data", userDTO);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	ResponseEntity<Map<String, Object>> findById(@Valid @RequestBody LoginDTO loginDTO) {
		Map<String, Object> response = new HashMap<>();

		try {
			UserDTO validatedDTO = userRepository.validate(loginDTO);
			String jwtToken = jwtService.generateJWTToken(validatedDTO);

			response.put("success", true);
			response.put("token", jwtToken);
			response.put("data", validatedDTO);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Invalid email and password combination");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/signup")
	ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
		Map<String, Object> response = new HashMap<>();
		Integer createdUserId = null;
		try {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
			if (!pattern.matcher(signUpDTO.email()).matches())
				throw new RuntimeException(String.format("Email '%s' is invalid", signUpDTO.email()));

			Integer emailCount = userRepository.findByEmail(signUpDTO.email());
			if (emailCount > 0) {
				response.put("success", false);
				response.put("message", String.format("User with email '%s' already exist", signUpDTO.email()));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			createdUserId = userRepository.createUser(signUpDTO);
			UserDTO userDTO = userRepository.findByUserId(createdUserId);
			response.put("success", true);
			response.put("data", userDTO);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "Sign up failed, but don't fret - you can retry.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		UserDTO createdUser = userRepository.findByUserId(createdUserId);
		String jwtToken = jwtService.generateJWTToken(createdUser);
		response.put("token", jwtToken);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
