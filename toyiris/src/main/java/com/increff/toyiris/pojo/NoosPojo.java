package com.increff.toyiris.pojo;

import javax.persistence.*;

@Entity
@Table(name="output_noos")
public class NoosPojo {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String category;
    private String styleCode;
    private double styleRos;
    private double styleRevContri;

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

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public double getStyleRos() {
        return styleRos;
    }

    public void setStyleRos(double styleRos) {
        this.styleRos = styleRos;
    }

    public double getStyleRevContri() {
        return styleRevContri;
    }

    public void setStyleRevContri(double styleRevContri) {
        this.styleRevContri = styleRevContri;
    }
}
