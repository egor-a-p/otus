package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.*;

@Entity
@Data
@ToString(callSuper = true, exclude = {"user"})
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@Table(name = "phone")
@NamedQueries({
		@NamedQuery(name = "PhoneDataSet.readAll", query = "SELECT p FROM PhoneEntity p"),
		@NamedQuery(name = "PhoneDataSet.readByCode", query = "SELECT p FROM PhoneEntity p WHERE p.code = :code")
})
public class PhoneEntity extends BaseEntity {

	@Column(name = "code")
	private int code;

	@Column(name = "number")
	private String number;

	@ManyToOne
	@PrimaryKeyJoinColumn
	private UserEntity user;
}
