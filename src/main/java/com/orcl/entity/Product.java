package com.orcl.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="PRODUCTS")
public class Product implements Serializable {

	private static final long serialVersionUID = 119L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="my_PRODUCTS_SEQ")
    @SequenceGenerator(name="my_PRODUCTS_SEQ", sequenceName="PRODUCTS_SEQ")
	private long id;

	private String name;

	private int price;

	private int quality;

    @Version
    @Column(name="OPTLOCK")
    private long version;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date created;

    @ManyToOne
    @JoinColumn(name="USER_ID")
	private User user;

    @OneToMany(mappedBy="product", fetch = FetchType.EAGER)
    private List<Text> texts;

    public Product() {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}