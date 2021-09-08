package com.increff.toyiris.pojo;

import javax.persistence.*;
import java.time.LocalDate;

@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"date","skuId","storeId"})}
)
@Entity
public class SalesPojo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "skuId")
    private int skuId;
    @Column(name = "storeId")
    private int storeId;
    @Column(name = "quantity")
    private double quantity;
    @Column(name = "discount")
    private double discount;
    @Column(name = "revenue")
    private double revenue;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

}