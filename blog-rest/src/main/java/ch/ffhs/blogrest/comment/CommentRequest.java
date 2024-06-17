package ch.ffhs.blogrest.comment;

public record CommentRequest(String content, Long postId) {
}
