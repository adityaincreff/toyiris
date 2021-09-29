package com.increff.toyiris.service;

import com.increff.toyiris.dao.StyleDao;
import com.increff.toyiris.pojo.StylePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
public class StyleService {

    @Autowired
    private StyleDao styleDao;

    public int select(String styleCode) throws ApiException {
        if (styleCode == "") {
            throw new ApiException("One or more fields are empty");
        }
        StylePojo stylePojo = styleDao.select(styleCode);
        if (stylePojo == null) {
            throw new ApiException("No such product exists.");
        }
        return stylePojo.getId();
    }

    public String selectbyId(int styleId) {
        return styleDao.selectById(styleId).getStyleCode();
    }

   // public void exists(StylePojo input) throws ApiException {
        //StylePojo stylePojo=styleDao.select(input.getStyleCode());
        //if(stylePojo!=null){
          //  throw new ApiException("Product with same Style Code already exists");
      //  }

    //}
    @Transactional
    public void add(StylePojo stylePojo) {
        StylePojo stylePojo1=styleDao.select(stylePojo.getStyleCode());
        if(stylePojo1!=null){
            stylePojo1.setBrand(stylePojo.getBrand());
            stylePojo1.setSubCategory(stylePojo.getSubCategory());
            stylePojo1.setCategory(stylePojo.getCategory());
            stylePojo1.setMrp(stylePojo.getMrp());
            stylePojo1.setGender(stylePojo.getGender());
            styleDao.update(stylePojo1);
        }
        else {
            styleDao.add(stylePojo);
        }
    }

    public List<StylePojo> selectAll() {
        return styleDao.selectAll();
    }

    public HashMap<Integer, StylePojo> selectAllMap() {
        List<StylePojo> stylePojo=styleDao.selectAll();
        HashMap<Integer,StylePojo> styleMap=new HashMap<Integer,StylePojo>();
        for(StylePojo p:stylePojo){
            styleMap.put(p.getId(),p);
        }
        return styleMap;
    }
}
