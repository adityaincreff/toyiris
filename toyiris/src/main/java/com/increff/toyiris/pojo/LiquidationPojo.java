package com.increff.toyiris.pojo;
import javax.persistence.*;

@Entity
@Table(name="output_liquidation")
public class LiquidationPojo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String category;
    private String subcategory;
    private double avgDiscount;
    private double avgCleanedDiscount;
    private double revenueCleanup;

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

    public double getAvgDiscount() {
        return avgDiscount;
    }

    public void setAvgDiscount(double avgDiscount) {
        this.avgDiscount = avgDiscount;
    }

    public double getAvgCleanedDiscount() {
        return avgCleanedDiscount;
    }

    public void setAvgCleanedDiscount(double avgCleanedDiscount) {
        this.avgCleanedDiscount = avgCleanedDiscount;
    }

    public double getRevenueCleanup() {
        return revenueCleanup;
    }

    public void setRevenueCleanup(double revenueCleanup) {
        this.revenueCleanup = revenueCleanup;
    }
}