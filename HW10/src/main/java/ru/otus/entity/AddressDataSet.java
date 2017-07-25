package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Data
@Entity
@Table(name = "address")
@EqualsAndHashCode(callSuper = true)
public class AddressDataSet extends DataSet{

	@Column(name = "street")
	private String street;

	@Column(name = "index")
	private int index;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private UserDataSet user;
}
