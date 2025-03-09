package dev.chiorji.user;

import dev.chiorji.execption.*;
import dev.chiorji.post.*;
import dev.chiorji.post.models.*;
import dev.chiorji.user.models.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final PostService postService;

	public UserService(UserRepository userRepository, PostService postService) {
		this.userRepository = userRepository;
		this.postService = postService;
	}

	public List<UserDTO> getAllUsers(String role, String email) {
		log.info("{} <--> {}", role, email);
		if (!email.isBlank() && !role.isBlank() && "ADMIN".equalsIgnoreCase(role)) {
			User user = findUserByEmailAndRole(email, role);
			if (user == null) throw new AuthenticationFailedException("Operation denied");
			return getActiveAndInActiveUsers();
		} else return getAllActiveUsers();
	}

	public User findUserByEmailAndRole(String email, String role) {
		return userRepository.findUserByEmailAndRole(email, role);
	}

	private List<UserDTO> getActiveAndInActiveUsers() {
		return userRepository.getActiveAndInActiveUsers();
	}

	private List<UserDTO> getAllActiveUsers() {
		return userRepository.getAllActiveUsers();
	}

	public Boolean softDeleteUserById(Integer id) {
		UserDTO userDTO = findUserById(id);
		if (userDTO == null) throw new User404Exception("User Not Found");
		List<PostDTO> postDTOS = postService.getPostByAuthorId(userDTO.id());
		for (PostDTO postDTO : postDTOS) {
			PostDeleteDTO postDeleteDTO = new PostDeleteDTO(postDTO.id(), id);
			postService.softDeletePostByIdAndAuthorId(postDeleteDTO);
		}
		return userRepository.softDeleteUserById(id);
	}

	public UserDTO findUserById(Integer userId) {
		return userRepository.findByUserId(userId);
	}

	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}
}
