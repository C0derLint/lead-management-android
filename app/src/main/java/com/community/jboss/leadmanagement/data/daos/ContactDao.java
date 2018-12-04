package com.community.jboss.leadmanagement.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.community.jboss.leadmanagement.data.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);

    @Update
    void update(Contact contact);

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact getContact(String id);

    @Query("SELECT * FROM contact")
    LiveData<List<Contact>> getLiveContacts();

    @Query("SELECT * FROM contact")
    List<Contact> getContacts();


}
