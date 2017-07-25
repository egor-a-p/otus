package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Data
@Entity
@Table(name = "phone")
@EqualsAndHashCode(callSuper = true)
public class PhoneDataSet extends DataSet {

	@Column(name = "code")
	private int code;

	@Column(name = "number")
	private String number;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private UserDataSet user;
}
