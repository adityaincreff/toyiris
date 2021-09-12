package com.increff.toyiris.service;

import com.increff.toyiris.dao.ReportDao;
import com.increff.toyiris.pojo.GoodSizePojo;
import com.increff.toyiris.pojo.LiquidationPojo;
import com.increff.toyiris.pojo.NoosPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReportService {
    @Autowired
    private ReportDao reportDao;
    public void addLiquidation(LiquidationPojo liquidationPojo){
        reportDao.addLiquidation(liquidationPojo);
    }
    public List<LiquidationPojo> selectAllLiquidation() {
    return reportDao.selectAllLiquidation();
    }

    public List<NoosPojo> selectAllNoos() {
        return reportDao.selectAllNoos();
    }

    public List<GoodSizePojo> selectAllIdentification() {
        return reportDao.selectAllIdentification();
    }

    public void deleteLiquidation() {
        reportDao.deleteLiquidation();

    }

    public void deleteIdentification() {
        reportDao.deleteIdentification();
    }

    public void addIdentification(GoodSizePojo goodSizesPojo) {
    reportDao.deleteIdentification();
    reportDao.addIdentification(goodSizesPojo);
    }

    public void deleteNoos() {
        reportDao.deleteNoos();
    }

    public void addNoos(NoosPojo noosPojo) {
        reportDao.addNoos(noosPojo);
    }
}
