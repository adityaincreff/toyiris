package com.increff.toyiris.module;

import com.increff.toyiris.model.SalesData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class ModuleData {
    private List<SalesData> cleanedSales;

    public List<SalesData> getCleanedSales() {
        return cleanedSales;
    }

    public void setCleanedSales(List<SalesData> cleanedSales) {
        this.cleanedSales = cleanedSales;
    }

    public void clear() {
        cleanedSales = null;
    }
}
