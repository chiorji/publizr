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

	public List<UserDTO> getAllUsers() {
		return getActiveAndInActiveUsers();
//		if (hasAdminRight(roleInfo)) {
//			log.info("getAllUsers --> admin");
//			return getActiveAndInActiveUsers();
//		} else {
//			log.info("getAllUsers --> author or viewer");
//			return getAllActiveUsers();
//		}
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

	public Boolean softDeleteUserById(Integer id) {
		UserDTO userToDelete = findUserById(id);
		if (userToDelete == null) throw new User404Exception("User Not Found");

		// ensure it is user deleting own account or admin is deleting user's account
		List<PostDTO> postDTOS = postService.getPostByAuthorId(userToDelete.id());
		for (PostDTO postDTO : postDTOS) {
			PostDeleteDTO postDeleteDTO = new PostDeleteDTO(postDTO.id(), id);
			postService.softDeletePostByIdAndAuthorId(postDeleteDTO);
		}
		return userRepository.softDeleteUserById(id);
	}

	public UserDTO findUserById(Integer userId) {
		return userRepository.findByUserId(userId);
	}
}
