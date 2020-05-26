package com.shawn.smartrestaurant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shawn.smartrestaurant.db.entity.User;

import java.util.List;

/**
 *
 */
@Dao
public interface UserDao {

    /**
     *
     */
    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    User findById(String id);

    /**
     *
     */
    @Query("SELECT * FROM user")
    List<User> findAll();

    /**
     *
     */
    @Insert
    void insertAll(User... users);

    /**
     *
     */
    @Delete
    void delete(User user);

    /**
     *
     */
    @Query("DELETE FROM user")
    void deleteAll();

    /**
     *
     */
    @Update
    public void updateUsers(User... users);

    /**
     *
     */
    @Update
    public void updateUser(User user);
}
