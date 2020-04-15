package com.shawn.smartrestaurant.ui.signup;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shawn.smartrestaurant.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar_sign_up);
        toolbar.setLogo(R.drawable.ic_noun_waiter_1948515);
//        toolbar.setNavigationIcon(R.drawable.ic_noun_waiter_1948515);
        setSupportActionBar(toolbar);
//        getActionBar().setIcon(R.drawable.ic_noun_waiter_1948515);
//        openOptionsMenu();
    }
}
