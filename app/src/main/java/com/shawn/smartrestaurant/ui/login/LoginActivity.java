package com.shawn.smartrestaurant.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;
import com.shawn.smartrestaurant.ui.signup.SignUpActivity;

import java.util.Objects;


/**
 *
 */
public class LoginActivity extends AppCompatActivity {

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonRegister = findViewById(R.id.button_login_register);
        Button buttonLogin = findViewById(R.id.button_login);

        EditText userId = findViewById(R.id.editText_login_user_id);
        EditText password = findViewById(R.id.editText_login_password);

        // Set SIGN UP button behavior.
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Set LOGIN button behavior.
        buttonLogin.setOnClickListener(v -> {
            User user = new User();
            user.setId(userId.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());

            // Check empty.
            if (user.getId().isEmpty() || user.getPassword().isEmpty()) {
                new MaterialAlertDialogBuilder(this).setTitle("Failed").setMessage("User ID and Password could not be empty.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Block UI and show progress bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            findViewById(R.id.progressBar_login).setVisibility(View.VISIBLE);

            // TODO Add onFailureListener
            db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).get().addOnSuccessListener(documentSnapshot -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get user in LoginActivity.");

                User result = documentSnapshot.toObject(User.class);

                if (null == result) {
                    // Release blocking UI and hide progress bar
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    findViewById(R.id.progressBar_login).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(this).setTitle("Failed").setMessage("User ID or Password is wrong.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                if (user.getPassword().equals(Objects.requireNonNull(result).getPassword())) {
                    AppDatabase localDb = AppDatabase.getInstance(getApplicationContext());

                    localDb.userDao().deleteAll();
                    localDb.userDao().insertAll(result);

                    // Release blocking UI and hide progress bar
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    findViewById(R.id.progressBar_login).setVisibility(View.GONE);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Release blocking UI and hide progress bar
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    findViewById(R.id.progressBar_login).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(this).setTitle("Failed").setMessage("User ID or Password is wrong.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                }
            });
        });
    }
}
