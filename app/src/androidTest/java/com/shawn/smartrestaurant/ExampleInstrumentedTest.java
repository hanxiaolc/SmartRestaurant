package com.shawn.smartrestaurant;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.local.LocalDb;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    @Ignore
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.shawn.smartrestaurant", appContext.getPackageName());
    }

    @Test
    @Ignore
    public void testFileDir() {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Log.e("HANXIAO1: ", "〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜");
        Log.e("HANXIAO1: ", appContext.getFilesDir().getPath());
        Log.e("HANXIAO1: ", "-------------------------------");
        for (String s : appContext.fileList()) {
            Log.e("HANXIAO1: ", s);
        }

//        Log.e("HANXIAO1: ", "-------------------------------");
//        for (File f : appContext.getFilesDir().listFiles()) {
//            if (f.getPath().contains("images")) {
//                f.delete();
//                    //Log.e("HANXIAO1: ", String.valueOf(f.isDirectory()));
//            }
//        }
        Log.e("HANXIAO1: ", "〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜");


    }

    @Ignore
    @Test
    public void testSqlite() {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Log.e("HANXIAO1: ", "〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜");

        for (String s : appContext.databaseList()) {
            Log.e("HANXIAO1: ", s);
        }
        Log.e("HANXIAO1: ", "-------------------------------");
        AppDatabase localDb = AppDatabase.getInstance(appContext);
        List<User> users = localDb.userDao().findAll();

        for (User user : users) {
            Log.e("HANXIAO1: ", user.getEmail());
        }

        List<Dish> dishs = localDb.dishDao().findAll();

//        Room.databaseBuilder(
//                appContext,
//                AppDatabase.class,
//                LocalDb.DB_NAME).allowMainThreadQueries()
//                .addMigrations(MIGRATION_1_2)
//                .build();

        Log.e("HANXIAO1: ", "〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜");


    }

    @Ignore
    @Test
    public void testMigration() {

        Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE `Dish` (`id` TEXT NOT NULL, `dishCode` TEXT, `dishName` TEXT, `group` TEXT, `category` TEXT, `price` REAL, `numbers` INTEGER NOT NULL, `hasImage` INTEGER NOT NULL, `timeConsumption` INTEGER NOT NULL, `createTime` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, `createUser` TEXT, `updateUser` TEXT, PRIMARY KEY(`id`))");
            }
        };

        // Context of the app under test.
        Context context = ApplicationProvider.getApplicationContext();

        AppDatabase localDb = Room.databaseBuilder(
                context,
                AppDatabase.class,
                LocalDb.DB_NAME).addMigrations(MIGRATION_1_2).build();


        List<User> users = localDb.userDao().findAll();
        List<Dish> dishes = localDb.dishDao().findAll();
        for (User user : users) {
            System.out.println(user.getId());
        }
    }
}




