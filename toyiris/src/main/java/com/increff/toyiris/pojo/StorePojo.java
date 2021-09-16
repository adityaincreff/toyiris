package com.increff.toyiris.pojo;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"branch"})}
)
public class StorePojo {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="branch")
    private String branch;
    @Column(name="city")
    private String city;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
