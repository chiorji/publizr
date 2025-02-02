package dev.publisher.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    User findById(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException();
        return user.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody User user) {
        userRepository.save(user);
    }
}
