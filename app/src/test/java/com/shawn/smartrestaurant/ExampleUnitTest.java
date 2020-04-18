package com.shawn.smartrestaurant;

import com.google.firebase.firestore.FirebaseFirestore;

import junit.framework.TestResult;

import org.junit.Ignore;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }

    @Test
    //@Ignore
    public void test01() {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
        Matcher matcher = pattern.matcher("hanxiaolc@gmail.com");
        boolean b = matcher.matches();
        assertTrue(matcher.matches());
    }

    @Ignore
    @Test
    public void test02() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("restaurent01").get();
        //assertTrue(matcher.matches());
    }
}
