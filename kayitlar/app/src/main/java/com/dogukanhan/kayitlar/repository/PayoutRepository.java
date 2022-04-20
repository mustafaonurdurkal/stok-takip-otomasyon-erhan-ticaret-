package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.model.Payout;
import com.j256.ormlite.dao.Dao;


public class PayoutRepository extends AbstractRepository<Payout, Long> {

    public PayoutRepository(Dao<Payout, Long> dao) {
        super(dao);
    }

}
