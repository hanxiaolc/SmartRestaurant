package com.shawn.smartrestaurant.ui.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbar);

        // Get all the elements from the view.
        Button buttonBack = findViewById(R.id.button_sign_up_back);
        Button buttonSignUp = findViewById(R.id.button_sign_up);

        EditText userId = findViewById(R.id.editText_sign_up_user_id);
        EditText password = findViewById(R.id.editText_sign_up_password);
        EditText email = findViewById(R.id.editText_sign_up_email);
        EditText companyCode = findViewById(R.id.editText_sign_up_company);
        Switch isOwner = findViewById(R.id.switch_sign_up_manager);

        // Set Back button behavior
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Set Sign Up button behavior
        buttonSignUp.setOnClickListener(v -> {
            User user = new User(userId.getText().toString(), password.getText().toString(), companyCode.getText().toString(), email.getText().toString(), isOwner.isChecked());

            // Check empty
            if (user.checkIsEmpty()) {
                alertDisplay("Failed", "User ID, Password, Email and Group Code could not be empty.", (dialog, which) -> {
                });
                return;
            }

            // Validate User ID
            if (!user.validateUserId()) {
                alertDisplay("Failed", "You need to give a 4-16 alphabets or numbers for User ID.", (dialog, which) -> {
                });
                return;
            }

            // Validate Company Code
            if (!user.validateGroup()) {
                alertDisplay("Failed", "You need to give a 4 numbers for Restaurant Code.", (dialog, which) -> {
                });
                return;
            }

            // Validate Email
            if (!user.validateEmail()) {
                alertDisplay("Failed", "The Email input is not legal.", (dialog, which) -> {
                });
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(ShawnOrder.COLLECTION_RESTAURANTS).document(user.getGroup()).get().addOnCompleteListener(taskCheckGroup -> {
                if (taskCheckGroup.isSuccessful()) {

                    // Check group exist.
                    if (!Objects.requireNonNull(taskCheckGroup.getResult()).exists()) {
                        if (!user.isManager()) {
                            alertDisplay("Failed", "You must register as a manager if your Restaurant Code is first time registered.", (dialog, which) -> {
                            });
                            return;
                        }

                        // Set a group document.
                        db.collection(ShawnOrder.COLLECTION_RESTAURANTS).document(user.getGroup()).set(new HashMap()).addOnSuccessListener(aVoidSetGroup -> {

                            // Register account.
                            db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).set(user).addOnSuccessListener(aVoid -> {
                                alertDisplay("Successful", "Please login with your information.", (dialog, which) -> {
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                });
                            }).addOnFailureListener(e -> {
                                Log.w("FAILURE", "Error writing document", e);
                            });
                        });
                    } else {
                        db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).get().addOnCompleteListener(taskCheckUser -> {
                            if (taskCheckUser.isSuccessful()) {

                                // Check user exist.
                                if (Objects.requireNonNull(taskCheckUser.getResult()).exists()) {
                                    alertDisplay("Failed", "User had already existed.", (dialog, which) -> {
                                    });
                                    return;
                                }

                                // Register account.
                                db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).set(user).addOnSuccessListener(aVoid -> {
                                    alertDisplay("Successful", "Please login with your information.", (dialog, which) -> {
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    });
                                }).addOnFailureListener(e -> {
                                    Log.w("FAILURE", "Error writing document", e);
                                });
                            }
                        });
                    }
                }
            });
        });
    }

    /**
     *
     */
    private void alertDisplay(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", listener);
        AlertDialog alertDialogButton = builder.create();
        alertDialogButton.show();
    }
}
