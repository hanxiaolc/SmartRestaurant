package com.shawn.smartrestaurant.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class SignUpActivity extends AppCompatActivity {

    //
    private FirebaseFirestore db;

    //
    AtomicReference<String> randomGroupCode = new AtomicReference<>();

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get all the elements from the view.
        Button buttonBack = findViewById(R.id.button_sign_up_back);
        Button buttonSignUp = findViewById(R.id.button_sign_up);
        EditText userId = findViewById(R.id.editText_sign_up_user_id);
        EditText password = findViewById(R.id.editText_sign_up_password);
        EditText companyCode = findViewById(R.id.editText_sign_up_company);
        SwitchMaterial isOwner = findViewById(R.id.switchMaterial_sign_up_manager);

        companyCode.setText(this.createGroupCode());

        // Set Back button behavior
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Set Sign Up button behavior
        buttonSignUp.setOnClickListener(v -> {
            // Check group exist.
            if (!isOwner.isChecked()) {
                new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("You must register as a manager if your Restaurant Code is first time registered, or ask your manager for registering with his account.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            User user = new User();
            user.setId(userId.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.setGroup(companyCode.getText().toString().trim());
            user.setManager(isOwner.isChecked());
            user.setStatus("on");
            user.setCreateTime(new Date().getTime());
            user.setUpdateTime(new Date().getTime());

            // Check empty
            if (user.checkIsEmpty()) {
                new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("User ID, Password, Email and Group Code could not be empty.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            // Validate User ID
            if (!user.validateUserId()) {
                new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("You need to give a 4-16 alphabets or numbers for User ID.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            // Validate Company Code
            if (!user.validateGroup()) {
                new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("You need to give a 4 numbers for Restaurant Code.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            // Validate Email
            if (!user.validateEmail()) {
                new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("The Email input is not legal.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            this.db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).get().addOnCompleteListener(taskCheckUser -> {
                if (taskCheckUser.isSuccessful()) {
                    // Check user exist.
                    if (Objects.requireNonNull(taskCheckUser.getResult()).exists()) {
                        new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Failed").setMessage("User had already existed.").setPositiveButton("OK", (dialog, which) -> {
                        }).show();
                        return;
                    }

                    // Register account.
                    // TODO Add OnFailureListener
                    this.db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).set(user).addOnSuccessListener(aVoid -> {
                        new MaterialAlertDialogBuilder(getApplicationContext()).setTitle("Successful").setMessage("Please login with your information.").setPositiveButton("OK", (dialog, which) -> {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }).show();
                    });
                }
            });
        });
    }

    /**
     *
     */
    public String createGroupCode() {

        // Block UI and show progress bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.progressBar_sign_up).setVisibility(View.VISIBLE);

        this.db.collection(ShawnOrder.COLLECTION_OTHERS).get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<String> groupList = new ArrayList<>();
            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                groupList.add(Objects.requireNonNull(ds.toObject(Other.class)).getId());
            }

            Random random = new Random();
            do {
                this.randomGroupCode.set(this.fillOutGroupCode(random.nextInt(10000)));
            } while (groupList.contains(this.randomGroupCode.get()));
        });

        // Release blocking UI and hide progress bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.progressBar_sign_up).setVisibility(View.GONE);

        return this.randomGroupCode.get();
    }

    /**
     *
     */
    public String fillOutGroupCode(int groupCode) {
        StringBuilder result = new StringBuilder(String.valueOf(groupCode));
        for (int i = 0; i < 4 - result.length(); i++) {
            result.insert(0, "0");
        }

        return result.toString();
    }

    /**
     *
     */
    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     *
     */
    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }
}
