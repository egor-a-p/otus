package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Entity
@Getter
@Setter
@ToString(callSuper = true, exclude = {"user"})
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
}
