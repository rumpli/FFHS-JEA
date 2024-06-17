package ch.ffhs.jea.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "select u from User u where u.username = :username")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\": ").append(id).append(",");
        sb.append("\"username\": \"").append(username).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public User() {

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
