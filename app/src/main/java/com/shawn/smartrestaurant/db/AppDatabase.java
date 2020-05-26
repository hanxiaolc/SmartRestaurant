package com.shawn.smartrestaurant.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shawn.smartrestaurant.db.dao.UserDao;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.local.LocalDb;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     *
     */
    public abstract UserDao userDao();

    /**
     *
     */
//    public abstract DishDao dishDao();

    /**
     *
     */
//    public abstract OtherDao otherDao();

    /**
     *
     */
    public static AppDatabase getInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, LocalDb.DB_NAME).allowMainThreadQueries().build();
    }
}
