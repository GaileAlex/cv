package ee.gaile.entity.blog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.gaile.entity.users.Roles;
import ee.gaile.entity.users.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Comments")
@Table(name = "comments")
public class Comments implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_blog")
    @JsonIgnore
    private Blog blog;

    @Column(name = "comment", length = 2000)
    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private Date date;

    @ManyToOne(targetEntity = Users.class)
    @JoinTable(name = "comment_users",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Users users;

    @PrePersist
    void createdAt() {
        this.date = new Date();
    }
}
