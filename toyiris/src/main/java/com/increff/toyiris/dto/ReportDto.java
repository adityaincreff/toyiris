package com.increff.toyiris.dto;

import com.increff.toyiris.pojo.GoodSizePojo;
import com.increff.toyiris.pojo.LiquidationPojo;
import com.increff.toyiris.pojo.NoosPojo;
import com.increff.toyiris.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class ReportDto {
    @Autowired
    private ReportService service;
    public List<LiquidationPojo> selectAllLiquidation() {
        return service.selectAllLiquidation();
    }

    public List<NoosPojo> selectAllNoos() {
        return service.selectAllNoos();
    }

    public List<GoodSizePojo> selectAllIdentification() {
        return service.selectAllIdentification();
    }
}
