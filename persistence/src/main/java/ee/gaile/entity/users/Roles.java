package ee.gaile.entity.users;

import ee.gaile.entity.enums.EnumRoles;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "Roles")
@Table(name = "roles")
public class Roles {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "name")
	private EnumRoles name;

	public Roles(EnumRoles name) {
		this.name = name;
	}

}
