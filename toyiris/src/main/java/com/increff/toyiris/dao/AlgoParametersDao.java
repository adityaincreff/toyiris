package com.increff.toyiris.dao;

import com.increff.toyiris.pojo.AlgoInputPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
@Repository
public class AlgoParametersDao extends AbstractDao{
    private String SELECT_ALL ="Select p from AlgoInputPojo p";
    private String Delete_All="Delete from AlgoInputPojo";
    @Transactional
    public void add(AlgoInputPojo inputPojo) {
        em().persist(inputPojo);
    }
@Transactional
    public void delete() {
        em().createQuery(Delete_All).executeUpdate();
    }

    public List<AlgoInputPojo> selectAll() {
        TypedQuery<AlgoInputPojo> query=getQuery(SELECT_ALL,AlgoInputPojo.class);
        return query.getResultList();
    }


}
