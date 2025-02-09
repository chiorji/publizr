package dev.publizr.bootstrap;import com.fasterxml.jackson.core.type.TypeReference;import com.fasterxml.jackson.databind.ObjectMapper;import dev.publizr.user.UserRepository;import dev.publizr.user.models.UserRunner;import jakarta.annotation.Priority;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.boot.CommandLineRunner;import org.springframework.core.Ordered;import org.springframework.stereotype.Component;import java.io.InputStream;@Component@Priority(Ordered.HIGHEST_PRECEDENCE)public class UserBootstrap implements CommandLineRunner {	private final UserRepository userRepository;	private final ObjectMapper objectMapper;	private static final Logger log = LoggerFactory.getLogger(UserBootstrap.class);	public UserBootstrap(UserRepository userRepository, ObjectMapper objectMapper) {		this.userRepository = userRepository;		this.objectMapper = objectMapper;	}	@Override	public void run(String... args) throws Exception {		long size = userRepository.list().size();		if (size == 0) {			log.warn("No user found in the database, adding default users....");			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/users.json")) {				UserRunner users = objectMapper.readValue(inputStream, UserRunner.class);				userRepository.saveAll(users.users());			} catch (RuntimeException e) {				log.error("Error loading default users.");				throw new RuntimeException(e);			}		} else {			log.info("Database already contain user(s), skipping...");		}	}}