package dev.publizr.user;

import dev.publizr.jwt.JWTService;
import dev.publizr.user.models.LoginDTO;
import dev.publizr.user.models.SignUpDTO;
import dev.publizr.user.models.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserRepository userRepository;
	private final JWTService jwtService;

	public UserController(UserRepository userRepository, JWTService jwtService) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	@GetMapping("/list")
	ResponseEntity<Map<String, Object>> list() {
		Map<String, Object> responseMap = new HashMap<>();
		List<UserDTO> userDTO = userRepository.list();

		responseMap.put("success", true);
		responseMap.put("data", userDTO);

		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	@PostMapping("/login")
	ResponseEntity<Map<String, Object>> findById(@RequestBody LoginDTO payload) {
		Map<String, Object> responseMap = new HashMap<>();

		try {
			LoginDTO loginDTO = new LoginDTO(payload.email(), payload.password());
			UserDTO user = userRepository.findByEmailAndPassword(loginDTO);
			String jwtToken = jwtService.generateJWTToken(user);

			responseMap.put("success", true);
			responseMap.put("token", jwtToken);
			responseMap.put("data", user);

			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("message", "INVALID USERNAME OR PASSWORD");

			return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/signup")
	ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody SignUpDTO payload) {
		Map<String, Object> responseMap = new HashMap<>();
		Integer createdUserId = null;
		try {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
			if (!pattern.matcher(payload.email()).matches())
				throw new RuntimeException(String.format("EMAIL '%S' IS INVALID", payload.email().toUpperCase()));

			Integer emailCount = userRepository.findUserByEmailAddress(payload.email());
			if (emailCount > 0) {
				responseMap.put("success", String.valueOf(false));
				responseMap.put("message", String.format("USER WITH EMAIL '%S' ALREADY EXIST", payload.email()));
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
			createdUserId = userRepository.createUser(payload);
			UserDTO userDTO = userRepository.findByUserId(createdUserId);
			responseMap.put("data", userDTO);
		} catch (RuntimeException e) {
			responseMap.put("success", false);
			responseMap.put("message", String.format(e.getLocalizedMessage()));
			return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
		}

		UserDTO createdUser = userRepository.findByUserId(createdUserId);
		String jwtToken = jwtService.generateJWTToken(createdUser);
		responseMap.put("token", jwtToken);
		return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
	}
}
