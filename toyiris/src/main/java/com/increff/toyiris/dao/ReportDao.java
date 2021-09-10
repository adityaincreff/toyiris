package com.increff.toyiris.dao;

import com.increff.toyiris.pojo.GoodSizePojo;
import com.increff.toyiris.pojo.LiquidationPojo;
import com.increff.toyiris.pojo.NoosPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
@Repository
public class ReportDao extends AbstractDao {
    private String SELECT_ALL_LIQUIDATION = "Select p from LiquidationPojo p";
    private String SELECT_ALL_NOOS = "Select p from NoosPojo p";
    private String SELECT_ALL_Identification = "Select p from GoodSizesPojo p";
    public List<LiquidationPojo> selectAllLiquidation() {
        TypedQuery<LiquidationPojo> query = getQuery(SELECT_ALL_LIQUIDATION, LiquidationPojo.class);
        return query.getResultList();
    }

    public List<NoosPojo> selectAllNoos() {
        TypedQuery<NoosPojo> query = getQuery(SELECT_ALL_NOOS, NoosPojo.class);
        return query.getResultList();
    }

    public List<GoodSizePojo> selectAllIdentification() {
        TypedQuery<GoodSizePojo> query = getQuery(SELECT_ALL_Identification, GoodSizePojo.class);
        return query.getResultList();
    }
}
