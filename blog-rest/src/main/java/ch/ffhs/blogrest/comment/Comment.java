package ch.ffhs.blogrest.comment;

import ch.ffhs.blogrest.appuser.AppUser;
import ch.ffhs.blogrest.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Column(insertable = false, updatable = false)
    private Date createdAt;
    @Column(insertable = false)
    private Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne()
    private AppUser author;

    @PrePersist
    protected void onCreate() {
        createdAt = Date.valueOf(LocalDate.now());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Date.valueOf(LocalDate.now());
    }
}
