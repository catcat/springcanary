package com.orcl.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Entity

@Table(name="TEXTS"
       //,uniqueConstraints = {@UniqueConstraint(columnNames={"PRODUCT_ID", "PROPERTY_ID"})}
)
public class Text implements Serializable {
	private static final long serialVersionUID = 13L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_PRODUCTS_SEQ")
    @SequenceGenerator(name="my_PRODUCTS_SEQ", sequenceName="PRODUCTS_SEQ")
	private long id;

	private String value;


    @NaturalId
    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
	private Product product;

    @NaturalId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PROPERTY_ID")
    private Property property;

    @ManyToOne
    @JoinColumn(name="VARIANT_ID")
    private Variant variant;

    public Text() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text value = (Text) o;

        if (id != value.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}