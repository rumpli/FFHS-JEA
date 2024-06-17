package ch.ffhs.blogrest.comment;

import ch.ffhs.blogrest.appuser.AppUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment, Long authorId) {
        AppUser author = new AppUser();
        author.setId(authorId);
        comment.setAuthor(author);
        return commentRepository.saveAndFlush(comment);
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment update(Comment comment, Long commentId, Long authorId) {
        Comment existingComment = getById(commentId);
        if (!existingComment.getAuthor().getId().equals(authorId)) {
            throw new AccessDeniedException("You don't have permission to update this comment");
        }
        comment.setId(commentId);
        return save(comment, authorId);
    }

    public List<Comment> getAllByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getAllSorted() {
        return commentRepository.findAllSorted();
    }
}
