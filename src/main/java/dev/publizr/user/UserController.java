package dev.publizr.user;

import dev.publizr.jwt.JWTService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

	@GetMapping("/{id}")
	UserDTO findById(@PathVariable Integer id) {
		Optional<UserDTO> user = userRepository.findById(id);
		return user.orElse(null);
	}

	@PostMapping("")
	ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody User user) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		if (!pattern.matcher(user.email()).matches())
			throw new RuntimeException(String.format("EMAIL '%S' IS INVALID", user.email().toUpperCase()));

		Integer createdUserId = null;
		try {
			Integer emailCount = userRepository.findUserByEmailAddress(user.email());
			if (emailCount > 0)
				throw new RuntimeException(String.format("USER WITH EMAIL '%S' ALREADY EXIST", user.email()));

			createdUserId = userRepository.createUser(user);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

		User createdUser = userRepository.findByUserId(createdUserId);
		String jwtToken = jwtService.generateJWTToken(createdUser);
		Map<String, String> map = new HashMap<>();
		map.put("token", jwtToken);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
}
