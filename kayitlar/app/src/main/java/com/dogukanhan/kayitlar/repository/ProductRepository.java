package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.model.Product;
import com.j256.ormlite.dao.Dao;


public class ProductRepository extends AbstractRepository<Product, Long> {

    public ProductRepository(Dao<Product, Long> dao) {
        super(dao);
    }

}
