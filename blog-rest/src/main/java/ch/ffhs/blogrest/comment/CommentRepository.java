package ch.ffhs.blogrest.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.post.id = :postId order by c.createdAt desc")
    List<Comment> findByPostId(Long postId);

    @Query("select c from Comment c order by c.createdAt desc")
    List<Comment> findAllSorted();
}
