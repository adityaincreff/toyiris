package com.increff.toyiris.dao;

import com.increff.toyiris.pojo.SalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SalesDao extends AbstractDao {
    private String SELECT_ALL = "Select p from SalesPojo p";
    private String SELECT_BY_ID = "Select p from SalesPojo p where p.date=:date AND p.skuId=:skuId AND p.storeId=:storeId";

    @Transactional
    public void add(SalesPojo salesPojo) {
        em().persist(salesPojo);
    }

    public List<SalesPojo> selectAll() {
        TypedQuery<SalesPojo> query = getQuery(SELECT_ALL, SalesPojo.class);
        return query.getResultList();
    }


    @Transactional
    public SalesPojo selectByDateSkuStore(LocalDate date, int skuId, int storeId) {
        TypedQuery<SalesPojo> query = getQuery(SELECT_BY_ID, SalesPojo.class);
        query.setParameter("date", date).setParameter("skuId", skuId).setParameter("storeId", storeId);
        return getSingle(query);

    }
}