package com.increff.toyiris.service;

import com.increff.toyiris.dao.SalesDao;
import com.increff.toyiris.pojo.SalesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SalesService {
    @Autowired
    private SalesDao salesDao;
    public void add(SalesPojo salesPojo) {
        salesDao.add(salesPojo);
    }

    public List<SalesPojo> selectAll() {
        return salesDao.selectAll();
    }
}
