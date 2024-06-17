package ch.ffhs.blogrest.comment;

import ch.ffhs.blogrest.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
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

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(CommentController.PATH)
public class CommentController {
    public static final String PATH = "/comments";

    private final CommentService commentService;
    private final Mapper<CommentRequest, Comment> fromDto;
    private final Mapper<Comment, CommentResponse> toDto;

    @Autowired
    public CommentController(CommentService commentService, Mapper<CommentRequest, Comment> fromDto, Mapper<Comment, CommentResponse> toDto) {
        this.commentService = commentService;
        this.fromDto = fromDto;
        this.toDto = toDto;
    }

    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getComments(@RequestParam(required = false) Long postId) {
        if (postId != null) {
            return ResponseEntity.ok(commentService.getAllByPostId(postId).stream().map(toDto::map).collect(Collectors.toList()));
        }
        return ResponseEntity.ok(commentService.getAllSorted().stream().map(toDto::map).collect(Collectors.toList()));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDto.map(commentService.getById(id)));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with id " + id + " not found");
        }
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, Authentication authentication) {
        try {
            Comment comment = fromDto.map(commentRequest);
            Comment createdComment = commentService.save(comment, extractUserId(authentication.getPrincipal()));
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto.map(createdComment));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not create comment");
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {
        try {
            Comment comment = fromDto.map(commentRequest);
            Comment createdComment = commentService.update(comment, id, extractUserId(authentication.getPrincipal()));
            return ResponseEntity.ok(toDto.map(createdComment));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not create comment");
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only owner can update comments");
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with id " + id + " not found");
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
