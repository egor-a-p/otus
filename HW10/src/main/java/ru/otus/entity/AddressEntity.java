package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;

@Entity
@Data
@ToString(callSuper = true, exclude = {"user"})
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@Table(name = "address")
@NamedQuery(name = "AddressDataSet.readAll", query = "SELECT a FROM AddressEntity a")
public class AddressEntity extends BaseEntity {

	@Column(name = "street")
	private String street;

	@Column(name = "index")
	private int index;

	@OneToOne(mappedBy = "userAddress")
	private UserEntity user;
}
