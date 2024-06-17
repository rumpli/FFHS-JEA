package ch.ffhs.blogrest.comment;

import ch.ffhs.blogrest.Mapper;
import ch.ffhs.blogrest.NamedEntityReference;
import org.springframework.stereotype.Component;

@Component
public class CommentResponseMapper implements Mapper<Comment, CommentResponse> {
    @Override
    public CommentResponse map(Comment source) {
        return new CommentResponse(source.getId(), source.getContent(), source.getCreatedAt(), new NamedEntityReference<>(source.getAuthor().getId(), source.getAuthor().getUsername()));
    }
}
