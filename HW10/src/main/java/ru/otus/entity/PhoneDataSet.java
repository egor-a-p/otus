package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
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
}
