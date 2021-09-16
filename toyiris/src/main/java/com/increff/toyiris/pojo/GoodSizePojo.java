package com.increff.toyiris.pojo;


import javax.persistence.*;

@Entity
@Table(name="output_goodSize")
public class GoodSizePojo {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "category")
    private String category;
    @Column(name = "subcategory")
    private String subcategory;
    @Column(name="size")
    private String size;
    @Column(name="revContri")
    private double revContri;
    @Column(name="typeOfSizes")
    private String typeOfSizes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getRevContri() {
        return revContri;
    }

    public void setRevContri(double revContri) {
        this.revContri = revContri;
    }

    public String getTypeOfSizes() {
        return typeOfSizes;
    }

    public void setTypeOfSizes(String typeOfSizes) {
        this.typeOfSizes = typeOfSizes;
    }



}
