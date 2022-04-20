package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Sale;
import com.j256.ormlite.dao.Dao;


public class IncomeRepository extends AbstractRepository<Income, Long> {

    public IncomeRepository(Dao<Income, Long> dao) {
        super(dao);
    }

}
