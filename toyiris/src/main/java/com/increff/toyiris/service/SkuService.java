package com.increff.toyiris.service;

import com.increff.toyiris.dao.SkuDao;
import com.increff.toyiris.pojo.SkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Service
public class SkuService {
    @Autowired
    private SkuDao skuDao;
    public void exists(SkuPojo input) throws ApiException{
        SkuPojo skuPojo=skuDao.select(input.getSkuCode());
        if(skuPojo!=null)
        {
            throw new ApiException("SKU already exists.");
        }
    }

    public void add(SkuPojo skuPojo) {
        skuDao.add(skuPojo);
    }

    public List<SkuPojo> selectAll() {
        return skuDao.selectAll();
    }

    public int select(String skuCode) throws ApiException {
        SkuPojo skuPojo=skuDao.select(skuCode);
        if(skuPojo==null)
        {
            throw new ApiException("No such SKU exists.");
        }
        return skuPojo.getId();
    }

    public String selectById(int id) {
        return skuDao.selectById(id).getSkuCode();
    }

    public HashMap<Integer,SkuPojo> selectAllMap() {
        List<SkuPojo> list=skuDao.selectAll();
        HashMap<Integer,SkuPojo> skuMap=new HashMap<Integer,SkuPojo>();
        for(SkuPojo p:list){
            skuMap.put(p.getId(),p);
        }
        return skuMap;
    }
}
