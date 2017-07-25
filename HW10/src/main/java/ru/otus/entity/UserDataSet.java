package ru.otus.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by egor on 24.07.17.
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@NamedQueries({
	@NamedQuery(name = "UserDataSet.readByName", query = "SELECT u FROM UserDataSet u WHERE u.name = :name"),
	@NamedQuery(name = "UserDataSet.readAll", query = "SELECT u FROM UserDataSet u")
})
public class UserDataSet extends DataSet {

	@Column(name = "name", unique = true)
	private String name;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private AddressDataSet address;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	private List<PhoneDataSet> phones = new ArrayList<>();
}
