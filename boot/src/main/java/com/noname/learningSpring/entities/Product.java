/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: Product.java 2018-11-07 10:24 tkachenko $
 *****************************************************************/
package com.noname.learningSpring.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;


@Entity
@Table(name = "Products")
public class Product implements Serializable, IEntity<Product> {

    @Id
    @GeneratedValue
    private Long id;


    @NotNull
    @Column(name = "Code", length = 20, nullable = false)
    private String code;

    @NotNull
    @Column(name = "Name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Lob
    @Column(name = "Image")
    private Blob image;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Created_Date", updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public Product(@NotNull String name, @NotNull BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public String getCode() {
        return code;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public EntityReference<Product> toReference() {
        return new EntityReference<>(id, name, Product.class);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
