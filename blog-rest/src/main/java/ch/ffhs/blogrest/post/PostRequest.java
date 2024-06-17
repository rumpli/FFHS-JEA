package ch.ffhs.blogrest.post;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(@NotBlank(message = "Title must not be empty") String title, @NotBlank(message = "Content must not be empty") String content) {
}
