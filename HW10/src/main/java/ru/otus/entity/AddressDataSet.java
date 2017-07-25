package ru.otus.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Data
@Entity
@Table(name = "address")
@EqualsAndHashCode(callSuper = true)
@NamedQueries({
		@NamedQuery(name = "AddressDataSet.readAll", query = "SELECT a FROM AddressDataSet a"),
		@NamedQuery(name = "AddressDataSet.readByUser", query = "SELECT a FROM AddressDataSet a WHERE a.user = :user")
})
public class AddressDataSet extends DataSet{

	@Column(name = "street")
	private String street;

	@Column(name = "index")
	private int index;

	@OneToOne(mappedBy = "address")
	private UserDataSet user;
}
