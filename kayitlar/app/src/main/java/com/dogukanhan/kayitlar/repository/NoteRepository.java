package com.dogukanhan.kayitlar.repository;

import com.dogukanhan.kayitlar.model.Note;
import com.j256.ormlite.dao.Dao;

public class NoteRepository extends AbstractRepository<Note, Long>  {

    public NoteRepository(Dao<Note, Long> dao) {
        super(dao);
    }
}