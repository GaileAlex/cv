package ee.gaile.entity.users;

import ee.gaile.enums.EnumRoles;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "Users")
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name", "email"})})
public class Users {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EnumRoles role;

    public Users(String username, String email, String password, EnumRoles role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
