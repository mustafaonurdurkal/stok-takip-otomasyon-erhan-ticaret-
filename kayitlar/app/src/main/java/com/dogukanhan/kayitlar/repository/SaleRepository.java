package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Sale;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;


public class SaleRepository extends AbstractRepository<Sale, Long> {

    private final IncomeRepository incomeRepository;

    public SaleRepository(DatabaseHelper databaseHelper) throws SQLException {
        super(databaseHelper.getSaleDao());
        this.incomeRepository = databaseHelper.getIncomeRepository();
    }

    @Override
    public boolean remove(Sale sale) throws SQLException {

        sale = dao.queryForId(sale.getId());

        final ForeignCollection<Income> incomes = sale.getIncomes();

        for (Income income : incomes) {

            if (!incomeRepository.remove(income))
                return false;
        }

        return dao.delete(sale) == 1;
    }
}
