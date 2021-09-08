package com.increff.toyiris.service;

import com.increff.toyiris.dao.StyleDao;
import com.increff.toyiris.pojo.StylePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void exists(StylePojo input) throws ApiException {
        StylePojo stylePojo=styleDao.select(input.getStyleCode());
        if(stylePojo!=null){
            throw new ApiException("Product with same Style Code already exists");
        }
    }

    public void add(StylePojo stylePojo) {
        styleDao.add(stylePojo);
    }

    public List<StylePojo> selectAll() {
        return styleDao.selectAll();
    }
}
