package dev.publisher.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("")
    List<Post> findAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Post> findById(@PathVariable Integer id) {
        return postRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@RequestBody Post post) {
        postRepository.save(post);
    }

}
