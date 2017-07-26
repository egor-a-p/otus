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

/**
 * Created by egor on 24.07.17.
 */
@Entity
@Table(name = "users")
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AddressDataSet getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(AddressDataSet userAddress) {
		this.userAddress = userAddress;
	}

	public List<PhoneDataSet> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneDataSet> phones) {
		this.phones = phones;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		UserDataSet that = (UserDataSet) o;

		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserDataSet{" + "name='" + name + '\'' + '}';
	}
}
