package ch.ffhs.blogrest.comment;

import ch.ffhs.blogrest.Mapper;
import ch.ffhs.blogrest.post.Post;
import org.springframework.stereotype.Component;

@Component
public class CommentRequestMapper implements Mapper<CommentRequest, Comment> {
    @Override
    public Comment map(CommentRequest source) {
        Comment comment = new Comment();
        comment.setContent(source.content());

        Post post = new Post();
        post.setId(source.postId());
        comment.setPost(post);

        return comment;
    }
}
