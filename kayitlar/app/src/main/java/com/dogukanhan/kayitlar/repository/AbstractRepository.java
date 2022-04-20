package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.model.AbstractModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class AbstractRepository<M extends AbstractModel<K>, K> {

    protected final Dao<M, K> dao;

    protected AbstractRepository(Dao<M, K> dao) {
        this.dao = dao;
    }

    public List<M> findAll() throws SQLException {
        return dao.queryForAll();
    }

    public M findById(K k) throws SQLException {
        return dao.queryForId(k);
    }

    public M update(M m) throws SQLException {
        dao.update(m);
        return m;
    }

    public M create(M m) throws SQLException {
        dao.create(m);
        return m;
    }

    public boolean remove(M m) throws SQLException{
        return dao.delete(m) == 1;
    }

}
