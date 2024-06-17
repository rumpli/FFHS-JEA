package ch.ffhs.blogrest.post;

import ch.ffhs.blogrest.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(PostController.PATH)
public class PostController {
    public static final String PATH = "/posts";

    private final PostService postService;
    private final Mapper<Post, PostResponse> toDto;
    private final Mapper<PostRequest, Post> fromDto;


    @Autowired
    public PostController(PostService postService, Mapper<Post, PostResponse> toDto, Mapper<PostRequest, Post> fromDto) {
        this.postService = postService;
        this.toDto = toDto;
        this.fromDto = fromDto;
    }

    @GetMapping
    public ResponseEntity<Set<PostResponse>> getAll() {
        return ResponseEntity.ok(postService.getAll().stream().map(toDto::map).collect(Collectors.toSet()));
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(toDto.map(postService.getById(id)));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + id + " not found");
        }
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        try {
            Post post = fromDto.map(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto.map(postService.save(post, extractUserId(authentication.getPrincipal()))));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Post could not be created");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<PostResponse> update(@PathVariable("id") Long id, @RequestBody PostRequest postRequest, Authentication authentication) {
        try {
            Post post = fromDto.map(postRequest);
            return ResponseEntity.ok(toDto.map(postService.update(post, id, extractUserId(authentication.getPrincipal()))));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Post could not be updated");
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only owner can update this post");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            postService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with id " + id + " not found");
        }
    }

    private Long extractUserId(Object principal) {
        Long currentUserId;
        if (principal instanceof Jwt jwt) {
            currentUserId = jwt.getClaim("userId");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Only JWT tokens are supported");
        }
        return currentUserId;
    }
}
