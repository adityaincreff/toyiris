package com.increff.toyiris.service;

import com.increff.toyiris.dao.StoreDao;
import com.increff.toyiris.pojo.StorePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
public class StoreService {
    @Autowired
    private StoreDao storeDao;
    @Transactional
    public void add(StorePojo storePojo) {
        StorePojo storePojo1=storeDao.select(storePojo.getBranch());
        if(storePojo1!=null){
            storePojo1.setCity(storePojo.getCity());
            storeDao.update(storePojo1);
        }
        else{
            storeDao.add(storePojo);
        }
    }
    @Transactional
    public List<StorePojo> selectAll() {
        return storeDao.selectAll();
    }

    //public void exists(StorePojo input) throws ApiException{
      //  StorePojo storePojo=storeDao.select(input.getBranch());
        //if(storePojo!=null){
        //    throw new ApiException("Store already exists");
       // }
    //}
    @Transactional
    public int select(String s) throws ApiException {
        StorePojo storePojo=storeDao.select(s);
        if(storePojo== null){
            throw new ApiException("No such Store exists");
        }
        return storePojo.getId();

    }
    @Transactional
    public String selectById(int id) {
        return storeDao.selectById(id).getBranch();
    }
}
