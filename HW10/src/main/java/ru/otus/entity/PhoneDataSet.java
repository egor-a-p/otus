package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author e.petrov. Created 07 - 2017.
 */

@Entity
@Table(name = "phone")
@NamedQueries({
		@NamedQuery(name = "PhoneDataSet.readAll", query = "SELECT p FROM PhoneDataSet p"),
		@NamedQuery(name = "PhoneDataSet.readByCode", query = "SELECT p FROM PhoneDataSet p WHERE p.code = :code")
})
public class PhoneDataSet extends DataSet {

	@Column(name = "code")
	private int code;

	@Column(name = "number")
	private String number;

	@ManyToOne
	@PrimaryKeyJoinColumn
	private UserDataSet user;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

		PhoneDataSet that = (PhoneDataSet) o;

		if (code != that.code) {
			return false;
		}
		return number != null ? number.equals(that.number) : that.number == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + code;
		result = 31 * result + (number != null ? number.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PhoneDataSet{" + "code=" + code + ", number='" + number + '\'' + '}';
	}
}
