package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Entity
@Table(name = "address")
@NamedQueries({
		@NamedQuery(name = "AddressDataSet.readAll", query = "SELECT a FROM AddressDataSet a"),
		@NamedQuery(name = "AddressDataSet.readByUser", query = "SELECT a FROM AddressDataSet a WHERE a.user = :user")
})
public class AddressDataSet extends DataSet{

	@Column(name = "street")
	private String street;

	@Column(name = "index")
	private int index;

	@OneToOne(mappedBy = "userAddress")
	private UserDataSet user;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public UserDataSet getUser() {
		return user;
	}

	public void setUser(UserDataSet user) {
		this.user = user;
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

		AddressDataSet that = (AddressDataSet) o;

		if (index != that.index) {
			return false;
		}
		return street != null ? street.equals(that.street) : that.street == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (street != null ? street.hashCode() : 0);
		result = 31 * result + index;
		return result;
	}

	@Override
	public String toString() {
		return "AddressDataSet{" + "street='" + street + '\'' + ", index=" + index + '}';
	}
}
