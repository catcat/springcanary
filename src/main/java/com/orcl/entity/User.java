package com.orcl.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="USERS")
public class User implements Serializable {
	private static final long serialVersionUID = 14L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_USERS_SEQ")
    @SequenceGenerator(name="my_USERS_SEQ", sequenceName="USERS_SEQ")
	private long id;

	private String name;

	private int performance;

	private int salary;

	@OneToMany(mappedBy="user")
	private List<Product> products;

    public User() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}