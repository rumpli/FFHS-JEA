package ch.ffhs.blogrest.post;

import ch.ffhs.blogrest.NamedEntityReference;

import java.sql.Date;

public record PostResponse(Long id, String title, String content, Date createdAt, NamedEntityReference<Long> author) {
}
