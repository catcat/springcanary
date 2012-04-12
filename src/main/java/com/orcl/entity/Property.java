package com.orcl.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="PROPERTIES")
public class Property implements Serializable {
	private static final long serialVersionUID = 12L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_PRODUCTS_SEQ")
    @SequenceGenerator(name="my_PRODUCTS_SEQ", sequenceName="PRODUCTS_SEQ")
	private long id;

	private String name;

    @Transient
    private Text singleText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="TYPE_ID")
    private Type type;

    @OneToMany(mappedBy="property")
    private List<Text> texts;

    public Property() {
    }

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

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Text getSingleText() {
        return singleText;
    }

    public void setSingleText(Text singleText) {
        this.singleText = singleText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        if (id != property.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}