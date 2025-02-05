package dev.publizr.user;

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
    List<UserDTO> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    UserDTO findById(@PathVariable Integer id) {
        Optional<UserDTO> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody User user) {
        userRepository.save(user);
    }
}
