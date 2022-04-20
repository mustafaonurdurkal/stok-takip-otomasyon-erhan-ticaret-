package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Payout;
import com.dogukanhan.kayitlar.model.Purchase;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;


public class PurchaseRepository extends AbstractRepository<Purchase, Long> {

    private final PayoutRepository payoutRepository;

    public PurchaseRepository(DatabaseHelper databaseHelper) throws SQLException {
        super(databaseHelper.getPurchaseDao());
        this.payoutRepository = databaseHelper.getPayoutRepository();
    }

    @Override
    public boolean remove(Purchase purchase) throws SQLException {

        purchase = findById(purchase.getId());

        for (Payout payout : purchase.getPayouts()) {
            if (!payoutRepository.remove(payout))
                return false;
        }

        return super.remove(purchase);
    }
}
