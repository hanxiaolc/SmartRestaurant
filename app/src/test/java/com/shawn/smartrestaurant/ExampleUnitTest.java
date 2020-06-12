package com.shawn.smartrestaurant;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;

import junit.framework.TestResult;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }

    @Test
    @Ignore("Test Method")
    public void test01() {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
        Matcher matcher = pattern.matcher("hanxiaolc@gmail.com");
        boolean b = matcher.matches();
        assertTrue(matcher.matches());
    }

    @Ignore("Test Method")
    @Test
    public void test02() {
    }

    @Test
    @Ignore("Test Method")
    public void test03() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(10000));
        }
    }
}
