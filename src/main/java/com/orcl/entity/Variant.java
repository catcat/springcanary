package com.orcl.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity

@Table(name="VARIANTS")

public class Variant implements Serializable {
	private static final long serialVersionUID = 13L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_PRODUCTS_SEQ")
    @SequenceGenerator(name="my_PRODUCTS_SEQ", sequenceName="PRODUCTS_SEQ")
	private long id;

	private String value;

    @OneToMany(mappedBy="variant")
	private List<Text> text;


    @ManyToOne
    @JoinColumn(name="TYPE_ID")
    private Type type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Text> getText() {
        return text;
    }

    public void setText(List<Text> text) {
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variant value = (Variant) o;

        if (id != value.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}