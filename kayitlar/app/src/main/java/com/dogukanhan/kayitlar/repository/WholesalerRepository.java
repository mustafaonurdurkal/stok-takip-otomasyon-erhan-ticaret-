package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Purchase;
import com.dogukanhan.kayitlar.model.Wholesaler;

import java.sql.SQLException;


public class WholesalerRepository extends AbstractRepository<Wholesaler, Long> {

    private final PurchaseRepository purchaseRepository;

    public WholesalerRepository(DatabaseHelper databaseHelper) throws SQLException {
        super(databaseHelper.getWholesalerDao());
        this.purchaseRepository = databaseHelper.getPurchaseRepository();
    }

    @Override
    public boolean remove(Wholesaler wholesaler) throws SQLException {

        wholesaler = dao.queryForId(wholesaler.getId());

        for (Purchase purchase : wholesaler.getPurchases()) {
            if (!purchaseRepository.remove(purchase)) {
                return false;
            }
        }

        return super.remove(wholesaler);
    }
}
