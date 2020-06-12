package com.shawn.smartrestaurant;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.db.local.LocalDb;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    @Ignore("Test Method")
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.shawn.smartrestaurant", appContext.getPackageName());
    }

    @Test
    @Ignore("Test Method")
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

    @Test
    @Ignore("Test Method")
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

//        List<Dish> dishs = localDb.dishDao().findAll();

//        Room.databaseBuilder(
//                appContext,
//                AppDatabase.class,
//                LocalDb.DB_NAME).allowMainThreadQueries()
//                .addMigrations(MIGRATION_1_2)
//                .build();

        Log.e("HANXIAO1: ", "〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜〜");


    }

    @Test
    @Ignore("Test Method")
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


//        List<User> users = localDb.userDao().findAll();
//        List<Dish> dishes = localDb.dishDao().findAll();
//        for (User user : users) {
//            System.out.println(user.getId());
//        }
    }

    @Test
    @Ignore("Test Method")
    public void testDestructiveMigration() {

        // Context of the app under test.
        Context context = ApplicationProvider.getApplicationContext();

        AppDatabase localDb = Room
                .databaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class, LocalDb.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();


        List<User> users = localDb.userDao().findAll();
        for (User user : users) {
            System.out.println(user.getId());
        }
    }

    @Test
    @Ignore("Test Method")
    public void testUpdateUser() {

        AppDatabase localDb = AppDatabase.getInstance(ApplicationProvider.getApplicationContext());
        List<User> users = localDb.userDao().findAll();

        for (User user : users) {
            if (user.getId().equals("user01")) {
                user.setCreateUser("user01");
                user.setUpdateUser("user01");
            }
            localDb.userDao().updateUser(user);
        }
    }

    @Test
    @Ignore("Test Method")
    public void testDeleteUser() {

        AppDatabase localDb = AppDatabase.getInstance(ApplicationProvider.getApplicationContext());
        localDb.userDao().deleteAll();
    }

    @Ignore("Test Method")
    @Test
    public void testDeleteGroup() {

        AppDatabase localDb = AppDatabase.getInstance(ApplicationProvider.getApplicationContext());
        localDb.userDao().deleteAll();

        String group = "8879";
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_GROUP, group).get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                db.collection(ShawnOrder.COLLECTION_DISHES).document(Objects.requireNonNull(ds.toObject(Dish.class)).getId()).delete();
            }
        });

        db.collection(ShawnOrder.COLLECTION_HISTORY).whereEqualTo(Table.COLUMN_GROUP, group).get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                db.collection(ShawnOrder.COLLECTION_HISTORY).document(String.valueOf(Objects.requireNonNull(ds.toObject(Table.class)).getUpdateTime())).delete();
            }
        });

        db.collection(ShawnOrder.COLLECTION_OTHERS).document(group).delete();


        db.collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Dish.COLUMN_GROUP, group).get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                Table table = ds.toObject(Table.class);
                db.collection(ShawnOrder.COLLECTION_TABLES).document(Objects.requireNonNull(table).getGroup() + "_" + table.getId()).delete();
            }
        });

        db.collection(ShawnOrder.COLLECTION_USERS).whereEqualTo(Dish.COLUMN_GROUP, group).get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                db.collection(ShawnOrder.COLLECTION_USERS).document(Objects.requireNonNull(ds.toObject(User.class)).getId()).delete();
            }
        });
    }
}




