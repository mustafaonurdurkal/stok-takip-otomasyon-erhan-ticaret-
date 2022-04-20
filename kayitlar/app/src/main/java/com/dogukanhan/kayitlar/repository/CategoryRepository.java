package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.model.Category;
import com.j256.ormlite.dao.Dao;


public class CategoryRepository extends AbstractRepository<Category, Long> {

    public CategoryRepository(Dao<Category, Long> dao) {
        super(dao);
    }

}
