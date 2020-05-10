package com.shawn.smartrestaurant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shawn.smartrestaurant.db.entity.Other;

import java.util.List;

/**
 *
 */
@Dao
public interface OtherDao {

    /**
     *
     */
    @Query("SELECT * FROM other WHERE id = :id LIMIT 1")
    Other findById(String id);

    /**
     *
     */
    @Query("SELECT * FROM other")
    List<Other> findAll();

    /**
     *
     */
    @Insert
    void insertAll(Other... others);

    /**
     *
     */
    @Delete
    void delete(Other other);

    /**
     *
     */
    @Query("DELETE FROM other")
    void deleteAll();
}
