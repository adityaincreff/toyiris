package com.increff.toyiris.pojo;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="algoInput")
public class AlgoInputPojo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="liquidationMulitplier")
   private double liquidationMultiplier;
   @Column(name="date")
    private LocalDate date;
   @Column(name="goodSize")
   private double goodSize;
   @Column(name="badSize")
   private double badSize;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public double getLiquidationMultiplier() {
        return liquidationMultiplier;
    }

    public void setLiquidationMultiplier(double liquidationMultiplier) {
        this.liquidationMultiplier = liquidationMultiplier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String end) {
        if(end==null || end==""){
            this.date = LocalDate.now();
        }
        else {
            this.date = LocalDate.parse(end);
        }
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public double getGoodSize() {
        return goodSize;
    }

    public void setGoodSize(double goodSize) {
        this.goodSize = goodSize;
    }

    public double getBadSize() {
        return badSize;
    }

    public void setBadSize(double badSize) {
        this.badSize = badSize;
    }

}
