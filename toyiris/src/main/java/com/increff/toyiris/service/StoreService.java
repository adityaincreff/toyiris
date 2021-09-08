package com.increff.toyiris.service;

import com.increff.toyiris.dao.StoreDao;
import com.increff.toyiris.pojo.StorePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StoreService {
    @Autowired
    private StoreDao storeDao;

    public void add(StorePojo storePojo) {
        storeDao.add(storePojo);
    }

    public List<StorePojo> selectAll() {
        return storeDao.selectAll();
    }

    public void exists(StorePojo input) throws ApiException{
        StorePojo storePojo=storeDao.select(input.getBranch());
        if(storePojo!=null){
            throw new ApiException("Store already exists");
        }
    }

    public int select(String s) throws ApiException {
        StorePojo storePojo=storeDao.select(s);
        if(storePojo== null){
            throw new ApiException("No such Store exists");
        }
        return storePojo.getId();

    }
}
