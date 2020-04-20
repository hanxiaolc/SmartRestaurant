package com.shawn.smartrestaurant.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shawn.smartrestaurant.models.User;

import java.util.List;

public interface UserDao {
    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    User findById(String id);

    @Query("SELECT * FROM user")
    List<User> findAll();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();
}
