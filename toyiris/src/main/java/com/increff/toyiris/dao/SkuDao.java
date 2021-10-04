package com.increff.toyiris.dao;

import com.increff.toyiris.pojo.SalesPojo;
import com.increff.toyiris.pojo.SkuPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
@Repository
public class SkuDao extends AbstractDao {
    private static String SELECT="Select p from SkuPojo p where p.skuCode=:skuCode";
    private static String SELECT_ALL="Select p from SkuPojo p";
    private static String SELECT_BY_ID = "Select p from SkuPojo p where p.id=:id";


    @Transactional
    public void add(SkuPojo skuPojo) {
        em().persist(skuPojo);
    }

    public SkuPojo select(String skuCode) {
        TypedQuery<SkuPojo> query = getQuery(SELECT, SkuPojo.class);
        query.setParameter("skuCode", skuCode);
        return getSingle(query);
    }

    public List<SkuPojo> selectAll() {
        TypedQuery<SkuPojo> query = getQuery(SELECT_ALL, SkuPojo.class);
        return query.getResultList();
    }

    public SkuPojo selectById(int id) {
        TypedQuery<SkuPojo> query = getQuery(SELECT_BY_ID, SkuPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
    @Transactional
    public void update(SkuPojo skuPojo1) {
    em().merge(skuPojo1);
    }


}
