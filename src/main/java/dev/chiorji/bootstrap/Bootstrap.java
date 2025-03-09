package dev.chiorji.bootstrap;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import dev.chiorji.auth.*;
import dev.chiorji.post.*;
import dev.chiorji.post.models.*;
import dev.chiorji.user.*;
import java.io.*;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.stereotype.*;

@Component
public class Bootstrap implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final ObjectMapper objectMapper;
	private final AuthService authService;

	public Bootstrap(UserRepository userRepository, PostRepository postRepository, ObjectMapper objectMapper, AuthService authService) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.objectMapper = objectMapper;
		this.authService = authService;
	}


	@Override
	public void run(String... args) throws Exception {
		loadUsers();
//		loadPosts();
	}

	void loadUsers() {
		Integer totalEntries = userRepository.totalEntries();
		if (totalEntries == 0) {
			log.warn("No user found in the database, adding default users....");
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/users.json")) {
				BulkSignUp users = objectMapper.readValue(inputStream, BulkSignUp.class);
				authService.saveAll(users.users());
			} catch (RuntimeException | IOException e) {
				log.error("Error loading default users.");
				throw new RuntimeException(e);
			}
		} else {
			log.info("Database already contain user(s), skipping...");
		}
	}

	void loadPosts() {
		Integer totalEntries = postRepository.totalEntries();
		if (totalEntries == 0) {
			log.warn("No post found in the database, adding default posts....");
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/posts.json")) {
				PostRunner posts = objectMapper.readValue(inputStream, PostRunner.class);
				postRepository.saveAll(posts.posts());
			} catch (RuntimeException | IOException e) {
				log.error("Error loading default posts.");
				throw new RuntimeException(e);
			}
		} else {
			log.info("Database already contain post(s), skipping...");
		}
	}
}
