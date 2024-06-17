package ch.ffhs.blogrest.post;

import ch.ffhs.blogrest.Mapper;
import ch.ffhs.blogrest.NamedEntityReference;
import org.springframework.stereotype.Component;

@Component
public class PostResponseMapper implements Mapper<Post, PostResponse> {

    @Override
    public PostResponse map(Post source) {
        return new PostResponse(source.getId(), source.getTitle(), source.getContent(), source.getCreatedAt(), new NamedEntityReference<>(source.getAuthor().getId(), source.getAuthor().getUsername()));
    }
}
