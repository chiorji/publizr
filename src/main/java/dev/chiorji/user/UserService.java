package dev.chiorji.user;

import dev.chiorji.config.*;
import dev.chiorji.user.models.*;
import java.util.*;
import java.util.regex.*;
import org.mindrot.jbcrypt.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final JWTService jwtService;

	public UserService(UserRepository userRepository, JWTService jwtService) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	public void saveAll(List<SignUpDTO> signUpDTO) {
		signUpDTO.forEach(this::processSignUpRequest);
	}

	public LoginUpResponseDTO processSignUpRequest(SignUpDTO signUpDTO) {
		try {

			if (!isValidEmailAddress(signUpDTO.email())) throw new RuntimeException("Email is invalid");
			Integer emailExists = userRepository.providedEmailExists(signUpDTO.email());
			if (emailExists > 0) throw new RuntimeException("Email already exist, please login to continue");

			String passwordHash = getPasswordHashString(signUpDTO.password());
			SignUpDTO signUpDTO1 = new SignUpDTO(signUpDTO.username(), signUpDTO.email(), passwordHash);
			KeyHolder keyHolder = userRepository.createUser(signUpDTO1);

			Integer userId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
			return generateSignInResponse(userId);
		} catch (RuntimeException e) {
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
		return userRepository.findByUserId(userId);
	}

	public LoginUpResponseDTO processLoginRequest(LoginDTO loginDTO) {
		UserDTO userDTO = validateLoginData(loginDTO);
		if (userDTO != null) return generateSignInResponse(userDTO.id());
		return null;
	}

	public UserDTO validateLoginData(LoginDTO payload) {
		try {
			User user = findUserByEmail(payload.email());
			if (!isValidLoginCredentials(payload.password(), user.password())) {
				throw new RuntimeException("There's a catch! An invalid email and password combination");
			}
			return new UserDTO(user.id(), user.username(), user.email(), user.role(), user.image_url(), user.created_at(), user.updated_at());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	private Boolean isValidLoginCredentials(String plaintext, String hash) {
		return BCrypt.checkpw(plaintext, hash);
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.getAllUsers();
	}

	public Boolean processPasswordReset(LoginDTO loginDTO) {
		try {
			Boolean isValidEmail = isValidEmailAddress(loginDTO.email());
			if (!isValidEmail) throw new RuntimeException("Invalid email provided");

			User user = findUserByEmail(loginDTO.email());
			if (user == null) throw new RuntimeException("User does not exist");

			String passwordHash = getPasswordHashString(loginDTO.password());
			LoginDTO update = new LoginDTO(loginDTO.email(), passwordHash);
			userRepository.updatePassword(update);
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
