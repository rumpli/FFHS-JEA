package ch.ffhs.blogrest.post;

import ch.ffhs.blogrest.appuser.AppUser;
import ch.ffhs.blogrest.comment.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 16000)
    private String content;
    @Column(updatable = false)
    private Date createdAt;
    @Column(insertable = false)
    private Date updatedAt;

    @ManyToOne()
    private AppUser author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @PrePersist
    protected void onCreate() {
        createdAt = Date.valueOf(LocalDate.now());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Date.valueOf(LocalDate.now());
    }
}
