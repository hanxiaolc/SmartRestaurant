package com.shawn.smartrestaurant.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.AppDatabase;
import com.shawn.smartrestaurant.models.User;
import com.shawn.smartrestaurant.models.firebase.ShawnOrder;
import com.shawn.smartrestaurant.models.local.LocalDb;
import com.shawn.smartrestaurant.ui.main.MainActivity;
import com.shawn.smartrestaurant.ui.signup.SignUpActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

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
                alertDisplay("Failed", "User ID and Password could not be empty.", (dialog, which) -> {
                });
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(task.getResult()).exists()) {
                        User result = Objects.requireNonNull(task.getResult()).toObject(User.class);

                        if (user.getId().equals(Objects.requireNonNull(result).getId()) && user.getPassword().equals(result.getPassword())) {
                            AppDatabase localDb = AppDatabase.getInstance(getApplicationContext());

                            localDb.userDao().deleteAll();
                            localDb.userDao().insertAll(user);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            alertDisplay("Failed", "User ID or Password is wrong.", (dialog, which) -> {
                            });
                        }
                    } else {
                        alertDisplay("Failed", "User ID is not exist.", (dialog, which) -> {
                        });
                    }
                }
            });
            // Toast.makeText(LoginActivity.this, user.getId() + user.getPassword(), Toast.LENGTH_LONG).show();
        });
    }

    /**
     *
     */
    private void alertDisplay(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", listener);
        AlertDialog alertDialogButton = builder.create();
        alertDialogButton.show();
    }
}
