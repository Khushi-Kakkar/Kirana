package com.kirana.register.model;

import java.math.BigDecimal;

import jakarta.persistence.*;


@Entity
@Table (name = "products")
public class Product {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    private String prodname;
    private BigDecimal prodprice;
    private String prodcategory;


    public Product() {}
    public Product(String prodname, BigDecimal prodprice, String prodcategory) {
        this.prodname = prodname;
        this.prodprice = prodprice;
        this.prodcategory = prodcategory;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProdname() {
        return prodname;
    }
    public void setProdname(String prodname) {
        this.prodname = prodname;
    }
    public BigDecimal getProdprice() {
        return prodprice;
    }
    public void setProdprice(BigDecimal prodprice) {
        this.prodprice = prodprice;
    }
    public String getProdcategory() {
        return prodcategory;
    }
    public void setProdcategory(String prodcategory) {
        this.prodcategory = prodcategory;
    }

}
