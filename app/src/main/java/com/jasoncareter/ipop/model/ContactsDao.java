package com.jasoncareter.ipop.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("select * from contacts")
    List<ContactsEntity> getAllContacts();

    @Insert
    void InsertOne(ContactsEntity entity);

    @Insert
    void Insert(ContactsEntity...entities);

    @Delete
    void deleteContact(ContactsEntity entity);

}
