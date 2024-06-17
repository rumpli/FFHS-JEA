package ch.ffhs.blogrest.comment;

import ch.ffhs.blogrest.NamedEntityReference;

import java.sql.Date;

public record CommentResponse(Long id, String content, Date createdAt, NamedEntityReference<Long> author) {
}
