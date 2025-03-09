package dev.chiorji.auth;

import dev.chiorji.config.*;
import dev.chiorji.execption.*;
import dev.chiorji.user.*;
import dev.chiorji.user.models.*;
import java.util.*;
import java.util.regex.*;
import org.mindrot.jbcrypt.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

@Service
public class AuthService {
	private final UserService userService;
	private final AuthRepository authRepository;
	private final JWTService jwtService;

	public AuthService(UserService userService, AuthRepository authRepository, JWTService jwtService) {
		this.userService = userService;
		this.authRepository = authRepository;
		this.jwtService = jwtService;
	}

	public void saveAll(List<SignUpDTO> signUpDTO) {
		signUpDTO.forEach(this::processSignUpRequest);
	}

	public LoginUpResponseDTO processSignUpRequest(SignUpDTO signUpDTO) {
		try {

			if (!isValidEmailAddress(signUpDTO.email())) throw new RuntimeException("Email is invalid");
			Integer emailExists = authRepository.providedEmailExists(signUpDTO.email());
			if (emailExists > 0) throw new RuntimeException("Email already exist, please login to continue");

			String passwordHash = getPasswordHashString(signUpDTO.password());
			SignUpDTO signUpDTO1 = new SignUpDTO(signUpDTO.username(), signUpDTO.email(), passwordHash);
			KeyHolder keyHolder = authRepository.createUser(signUpDTO1);

			Integer userId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
			return generateSignInResponse(userId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Boolean isValidEmailAddress(String email) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		return pattern.matcher(email).matches();
	}

	private String getPasswordHashString(String plaintext) {
		return BCrypt.hashpw(plaintext, BCrypt.gensalt(10));
	}

	private LoginUpResponseDTO generateSignInResponse(Integer userId) {
		UserDTO userDTO = findUserById(userId);
		String jwtToken = jwtService.generateJWTToken(userDTO);
		return new LoginUpResponseDTO(jwtToken, userDTO);
	}

	private UserDTO findUserById(Integer userId) {
		return userService.findUserById(userId);
	}

	public LoginUpResponseDTO processLoginRequest(LoginDTO loginDTO) {
		UserDTO userDTO = validateLoginData(loginDTO);
		if (userDTO != null) return generateSignInResponse(userDTO.id());
		throw new User404Exception("User does not exist");
	}

	public UserDTO validateLoginData(LoginDTO payload) {
		User user = userService.findUserByEmail(payload.email());
		if (!isValidLoginCredentials(payload.password(), user.password())) {
			throw new RuntimeException("An invalid email and password combination");
		}
		return new UserDTO(user.id(), user.username(), user.email(), user.role(), user.image_url(), user.created_at(), user.updated_at(), user.is_deleted());
	}

	private Boolean isValidLoginCredentials(String plaintext, String hash) {
		return BCrypt.checkpw(plaintext, hash);
	}

	public Boolean processPasswordReset(LoginDTO loginDTO) {
		if (!isValidEmailAddress(loginDTO.email())) throw new AuthenticationFailedException("Email is invalid");

		User user = userService.findUserByEmail(loginDTO.email());
		if (user == null) throw new User404Exception("User does not exist");

		String passwordHash = getPasswordHashString(loginDTO.password());
		LoginDTO update = new LoginDTO(loginDTO.email(), passwordHash);
		return authRepository.updatePassword(update);
	}
}
