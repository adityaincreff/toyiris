package com.increff.toyiris.dao;

import com.increff.toyiris.pojo.StylePojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
@Repository
public class StyleDao extends AbstractDao {
    private static String SELECT = "Select p from StylePojo p where p.styleCode=:styleCode";
    private static String SELECT_ALL = "Select p from StylePojo p";
    private static String SELECT_BY_ID = "Select p from StylePojo p where p.id=:id";

    @Transactional
    public void add(StylePojo stylePojo) {
        em().persist(stylePojo);
    }
    @Transactional
    public StylePojo select(String styleCode) {
        TypedQuery<StylePojo> query = getQuery(SELECT, StylePojo.class);
        query.setParameter("styleCode", styleCode);
        return getSingle(query);
    }

    public StylePojo selectById(int styleId) {
        TypedQuery<StylePojo> query = getQuery(SELECT_BY_ID, StylePojo.class);
        query.setParameter("id", styleId);
        return getSingle(query);
    }

    public List<StylePojo> selectAll() {
        TypedQuery<StylePojo> query = getQuery(SELECT_ALL, StylePojo.class);
        return query.getResultList();
    }
}
