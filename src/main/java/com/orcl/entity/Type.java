package com.orcl.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity

@Table(name="HTML_TYPES")

public class Type implements Serializable {
	private static final long serialVersionUID = 13L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_PRODUCTS_SEQ")
    @SequenceGenerator(name="my_PRODUCTS_SEQ", sequenceName="PRODUCTS_SEQ")
	private long id;

	private String name;

    @OneToMany(mappedBy="type")
	private List<Variant> variants;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type value = (Type) o;

        if (id != value.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}