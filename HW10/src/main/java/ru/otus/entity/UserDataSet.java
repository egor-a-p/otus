package ru.otus.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by egor on 24.07.17.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(callSuper = true, exclude = {"phones", "userAddress"})
@NamedQueries({
	@NamedQuery(name = "UserDataSet.readByName", query = "SELECT u FROM UserDataSet u WHERE u.name = :name"),
	@NamedQuery(name = "UserDataSet.readAll", query = "SELECT u FROM UserDataSet u")
})
public class UserDataSet extends DataSet {

	@Column(name = "name", unique = true)
	private String name;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private AddressDataSet userAddress;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PhoneDataSet> phones = new ArrayList<>();
}
