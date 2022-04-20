package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.config.DatabaseHelper;
import com.dogukanhan.kayitlar.model.Customer;

import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Sale;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;


public class CustomerRepository extends AbstractRepository<Customer, Long> {

    private SaleRepository saleRepository;

    public CustomerRepository(DatabaseHelper databaseHelper) throws SQLException {
        super(databaseHelper.getCustomerDao());
        this.saleRepository = databaseHelper.getSaleRepository();
    }

    @Override
    public boolean remove(Customer customer) throws SQLException {

        customer = dao.queryForId(customer.getId());

        final ForeignCollection<Sale> sales = customer.getSales();

        for (Sale sale : sales) {

            if (!saleRepository.remove(sale)) {
                return false;
            }
        }

        return dao.delete(customer) == 1;
    }
}
