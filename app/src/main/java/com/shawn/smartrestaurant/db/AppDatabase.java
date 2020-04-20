package com.shawn.smartrestaurant.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shawn.smartrestaurant.dao.UserDao;
import com.shawn.smartrestaurant.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     *
     */
    public abstract UserDao userDao();

    /**
     *
     */
    public static AppDatabase getInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, "database-name").build();
    }
}