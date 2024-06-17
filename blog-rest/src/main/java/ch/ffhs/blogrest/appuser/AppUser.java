package ch.ffhs.blogrest.appuser;

import ch.ffhs.blogrest.comment.Comment;
import ch.ffhs.blogrest.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<Post> posts;

    public AppUser(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
