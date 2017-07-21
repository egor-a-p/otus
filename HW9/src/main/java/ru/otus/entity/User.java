package ru.otus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Data
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "age", nullable = false)
	private int age;
}
