package dev.chiorji.auth;

import dev.chiorji.user.*;
import dev.chiorji.user.models.*;
import org.springframework.stereotype.*;

@Service
public class AuthService {
	private final UserService userService;

	public AuthService(UserService userService) {
		this.userService = userService;
	}

	public LoginUpResponseDTO processLoginRequest(LoginDTO loginDTO) {
		return userService.processLoginRequest(loginDTO);
	}

	public LoginUpResponseDTO processSignUpRequest(SignUpDTO signUpDTO) {
		return userService.processSignUpRequest(signUpDTO);
	}

	public Boolean processPasswordReset(LoginDTO loginDTO) {
		return userService.processPasswordReset(loginDTO);
	}
}
