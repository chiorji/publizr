package dev.chiorji.user;

import dev.chiorji.execption.*;
import dev.chiorji.post.*;
import dev.chiorji.post.models.*;
import dev.chiorji.user.models.*;
import dev.chiorji.util.*;
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

	public List<UserDTO> getAllUsers(RoleInfo roleInfo) {
		String email = roleInfo.email();
		String role = roleInfo.role();
		log.info("{} <--> {}", role, email);
		if (hasAdminRight(roleInfo)) {
			log.info("getAllUsers --> admin");
			return getActiveAndInActiveUsers();
		} else {
			log.info("getAllUsers --> author or viewer");
			return getAllActiveUsers();
		}
	}

	public Boolean hasAdminRight(RoleInfo roleInfo) {
		User user = findUserByEmail(roleInfo.email());
		return user.role().equalsIgnoreCase("ADMIN") && roleInfo.role().equalsIgnoreCase("ADMIN");
	}

	private List<UserDTO> getActiveAndInActiveUsers() {
		return userRepository.getActiveAndInActiveUsers();
	}

	private List<UserDTO> getAllActiveUsers() {
		return userRepository.getAllActiveUsers();
	}

	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	public Boolean softDeleteUserById(RoleInfo roleInfo, Integer id) {
		// ensure the user performing req exists
		User performer = findUserByEmail(roleInfo.email());
		if (performer == null) throw new AuthenticationFailedException("Request failed");

		UserDTO userToDelete = findUserById(id);
		if (userToDelete == null) throw new User404Exception("User Not Found");

		// ensure it is user deleting own account or admin is deleting user's account
		if (!hasAdminRight(roleInfo) || !Objects.equals(userToDelete.id(), id)) throw new AuthenticationFailedException("You cannot perform this action");
		List<PostDTO> postDTOS = postService.getPostByAuthorId(userToDelete.id());
		for (PostDTO postDTO : postDTOS) {
			PostDeleteDTO postDeleteDTO = new PostDeleteDTO(postDTO.id(), id);
			postService.softDeletePostByIdAndAuthorId(postDeleteDTO, roleInfo);
		}
		return userRepository.softDeleteUserById(id);
	}

	public UserDTO findUserById(Integer userId) {
		return userRepository.findByUserId(userId);
	}
}
