package ch.ffhs.blogrest.post;

import ch.ffhs.blogrest.appuser.AppUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAll() {
        return postRepository.getAllSorted();
    }

    public Post save(Post post, Long authorId) {
        AppUser author = new AppUser();
        author.setId(authorId);
        post.setAuthor(author);
        return postRepository.saveAndFlush(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public Post getById(Long id) {
        return postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Post update(Post post, Long postId, Long authorId) {
        Post existingPost = getById(postId);
        if (!existingPost.getAuthor().getId().equals(authorId)) {
            throw new AccessDeniedException("You do not have permission to update this post");
        }
        post.setId(existingPost.getId());
        return save(post, authorId);
    }
}
