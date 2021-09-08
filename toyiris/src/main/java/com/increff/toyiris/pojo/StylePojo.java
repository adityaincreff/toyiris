package com.increff.toyiris.pojo;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"styleCode"})}
)
public class StylePojo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "styleCode")
    private String styleCode;
    @Column(name = "brand")
    private String brand;
    @Column(name = "category")
    private String category;
    @Column(name = "subCategory")
    private String subCategory;
    @Column(name = "mrp")
    private double mrp;
    @Column(name = "gender")
    private String gender;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
