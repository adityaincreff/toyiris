package com.increff.toyiris.dto;

import com.increff.toyiris.AbstractUnitTest;
import com.increff.toyiris.pojo.StorePojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class StoreDtoTest extends AbstractUnitTest {
    @Autowired
    StoreDto storeDto;

    private StorePojo createStorePojo(String branch,String city)
    {
        StorePojo storePojo=new StorePojo();
        storePojo.setCity(city);
        storePojo.setBranch(branch);
        return storePojo;
    }
    @Test
    public void normalize(){
        StorePojo storePojo=createStorePojo("S1","B1");
          storeDto.normalize(storePojo);
        assertEquals("s1",storePojo.getBranch());
        assertEquals("b1",storePojo.getCity());
    }
    
}
