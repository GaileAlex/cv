package ee.gaile.entity.users;

import ee.gaile.entity.enums.EnumRoles;

import javax.persistence.*;

@Entity(name = "Roles")
@Table(name = "roles")
public class Roles {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "name")
	private EnumRoles name;

	public Roles() {
	}

	public Roles(EnumRoles name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EnumRoles getName() {
		return name;
	}

	public void setName(EnumRoles name) {
		this.name = name;
	}
}
