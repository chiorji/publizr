package dev.chiorji;

import io.github.cdimascio.dotenv.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
@EnableAutoConfiguration()
public class Application {
	public static void main(String[] args) {
		Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
		SpringApplication.run(Application.class, args);
	}
}
