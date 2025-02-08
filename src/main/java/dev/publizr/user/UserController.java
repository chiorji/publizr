package dev.publizr.user;

import dev.publizr.jwt.JWTService;
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


	@GetMapping("")
	List<UserDTO> findAll() {
		return userRepository.findAll();
	}

	@PostMapping("/login")
	ResponseEntity<Map<String, String>> findById(@RequestBody Map<String, String> payload) {
		String email = payload.get("email");
		String password = payload.get("password");

		UserDTO user = userRepository.findByEmailAndPassword(email, password);
		String jwtToken = jwtService.generateJWTToken(user);

		Map<String, String> map = new HashMap<>();
		map.put("token", jwtToken);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@PostMapping("/signup")
	ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody UserSignUpPayload payload) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		if (!pattern.matcher(payload.email()).matches())
			throw new RuntimeException(String.format("EMAIL '%S' IS INVALID", payload.email().toUpperCase()));

		Integer createdUserId = null;
		try {
			Integer emailCount = userRepository.findUserByEmailAddress(payload.email());
			if (emailCount > 0)
				throw new RuntimeException(String.format("USER WITH EMAIL '%S' ALREADY EXIST", payload.email()));

			createdUserId = userRepository.createUser(payload);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

		UserDTO createdUser = userRepository.findByUserId(createdUserId);
		String jwtToken = jwtService.generateJWTToken(createdUser);
		Map<String, String> map = new HashMap<>();
		map.put("token", jwtToken);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
}
