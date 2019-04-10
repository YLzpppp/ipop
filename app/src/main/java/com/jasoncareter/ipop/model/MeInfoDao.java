package com.jasoncareter.ipop.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface MeInfoDao {

    @Query("select * from MeInfoEntity")
    MeInfoEntity getAll();

    @Insert()
    void insert(MeInfoEntity... meInfoEntities);

    @Update()
    void update(MeInfoEntity meInfoEntity);

}
