package dev.publizr.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.publizr.post.PostRepository;
import dev.publizr.post.models.PostRunner;
import dev.publizr.user.UserRepository;
import dev.publizr.user.models.UserRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class Bootstrap implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final ObjectMapper objectMapper;

	public Bootstrap(UserRepository userRepository, PostRepository postRepository, ObjectMapper objectMapper) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public void run(String... args) throws Exception {
		loadUsers();
		loadPosts();
	}

	void loadUsers() {
		long totalEntries = userRepository.totalEntries();
		log.info("Total entries: {}", totalEntries);
		if (totalEntries == 0) {
			log.warn("No user found in the database, adding default users....");
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/users.json")) {
				UserRunner users = objectMapper.readValue(inputStream, UserRunner.class);
				userRepository.saveAll(users.users());
			} catch (RuntimeException | IOException e) {
				log.error("Error loading default users.");
				throw new RuntimeException(e);
			}
		} else {
			log.info("Database already contain user(s), skipping...");
		}
	}

	void loadPosts() {
		long totalEntries = postRepository.totalEntries();
		if (totalEntries == 0) {
			log.warn("No post found in the database, adding default posts....");
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/static/posts.json")) {
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
