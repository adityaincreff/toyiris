package com.increff.toyiris.dto;

import com.increff.toyiris.AbstractUnitTest;
import com.increff.toyiris.pojo.SkuPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class SkuDtoTest extends AbstractUnitTest {
    @Autowired
    SkuDto skuDto;

    private SkuPojo createSkuPojo(String skuCode, int styleId, String size) {
        SkuPojo skuPojo=new SkuPojo();
        skuPojo.setSkuCode(skuCode);
        skuPojo.setStyleId(styleId);
        skuPojo.setSize(size);
        return skuPojo;
    }

    @Test
    public void normalize() {
        SkuPojo skuPojo=createSkuPojo("LOLL",1,"LARGE");
        skuDto.normalize(skuPojo);
        assertEquals("loll",skuPojo.getSkuCode());
        assertEquals("large",skuPojo.getSize());
    }


}
