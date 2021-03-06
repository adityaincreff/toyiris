package com.increff.toyiris.service;

import com.increff.toyiris.dao.SalesDao;
import com.increff.toyiris.pojo.SalesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
public class SalesService {
    @Autowired
    private SalesDao salesDao;

@Transactional
    public void add(SalesPojo salesPojo) {

        salesDao.add(salesPojo);


    }
    @Transactional

    public List<SalesPojo> selectAll() {
        return salesDao.selectAll();
    }

    @Transactional

    public void delete(boolean truncate) {
        salesDao.deleteAll();
    }


     @Transactional
    public void exists(SalesPojo dataConverted) throws ApiException {
        SalesPojo salesPojo=salesDao.selectByDateSkuStore(dataConverted.getDate(),dataConverted.getSkuId(),dataConverted.getStoreId());
        if(salesPojo!=null){
            throw new ApiException("Sales for this date already exists");
        }
    }



}
