package ch.ffhs.blogrest.post;

import ch.ffhs.blogrest.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PostRequestMapper implements Mapper<PostRequest, Post> {
    @Override
    public Post map(PostRequest source) {
        Post post = new Post();
        post.setTitle(source.title());
        post.setContent(source.content());
        return post;
    }
}
