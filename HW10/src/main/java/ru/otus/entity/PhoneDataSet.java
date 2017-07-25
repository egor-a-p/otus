package ru.otus.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author e.petrov. Created 07 - 2017.
 */
@Data
@Entity
@Table(name = "phone")
@EqualsAndHashCode(callSuper = true)
@NamedQueries({
		@NamedQuery(name = "PhoneDataSet.readAll", query = "SELECT p FROM PhoneDataSet p"),
		@NamedQuery(name = "PhoneDataSet.readByCode", query = "SELECT p FROM PhoneDataSet p WHERE p.code = :code")
})
public class PhoneDataSet extends DataSet {

	@Column(name = "code")
	private int code;

	@Column(name = "number")
	private String number;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private UserDataSet user;
}
