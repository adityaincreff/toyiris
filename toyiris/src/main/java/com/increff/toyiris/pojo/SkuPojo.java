package com.increff.toyiris.pojo;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"skuCode"}), @UniqueConstraint(columnNames = {"styleId", "size"})}
)
public class SkuPojo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "skuCode")
    private String skuCode;
    @Column(name = "styleId")
    private int styleId;
    @Column(name = "size")
    private String size;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }



}