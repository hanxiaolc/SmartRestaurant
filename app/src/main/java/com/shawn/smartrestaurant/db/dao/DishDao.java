package com.shawn.smartrestaurant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shawn.smartrestaurant.db.entity.Dish;

import java.util.List;

/**
 *
 */
@Dao
public interface DishDao {

    /**
     *
     */
    @Query("SELECT * FROM dish WHERE id = :id LIMIT 1")
    Dish findById(String id);

    /**
     *
     */
    @Query("SELECT * FROM dish")
    List<Dish> findAll();

    /**
     *
     */
    @Insert
    void insertAll(Dish... dishes);

    /**
     *
     */
    @Insert
    void insert(Dish dish);

    /**
     *
     */
    @Delete
    void delete(Dish dish);

    /**
     *
     */
    @Query("DELETE FROM dish")
    void deleteAll();
}
