package com.shawn.smartrestaurant.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.login.LoginActivity;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 */
public class SignUpActivity extends AppCompatActivity {

    //
    private FirebaseFirestore db;

    /**
     *
     */
    public class DishIdTextWatcher implements TextWatcher {

        //
        private Context context;

        //
        private FirebaseFirestore db;


        /**
         *
         */
        DishIdTextWatcher(Context context, FirebaseFirestore db) {
            this.context = context;
            this.db = db;
        }

        /**
         *
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         *
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        /**
         *
         */
        @Override
        public void afterTextChanged(Editable s) {

            long currentTime = System.currentTimeMillis();

            EditText userId = findViewById(R.id.editText_sign_up_user_id);
            EditText password = findViewById(R.id.editText_sign_up_password);
            EditText companyCode = findViewById(R.id.editText_sign_up_company);
            SwitchMaterial manager = findViewById(R.id.switchMaterial_sign_up_manager);

            User user = new User();
            user.setId(userId.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.setGroup(companyCode.getText().toString().trim());
            user.setManager(manager.isChecked());
            user.setStatus("owner");
            user.setCreateUser(userId.getText().toString().trim());
            user.setUpdateUser(userId.getText().toString().trim());
            user.setCreateTime(currentTime);
            user.setUpdateTime(currentTime);

            this.db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).get().addOnSuccessListener(documentSnapshot -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get user to check if it has exited in SignUpActivity.");

                // Release blocking UI and hide progress bar
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                findViewById(R.id.progressBar_sign_up).setVisibility(View.GONE);

                // Check user exist.
                if (Objects.requireNonNull(documentSnapshot).exists()) {
                    new MaterialAlertDialogBuilder(this.context).setTitle("Failed").setMessage("User had already existed.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                Other other = new Other();
                other.setId(user.getGroup());
                other.setMenuVersion(currentTime);
                other.setTableVersion(currentTime);
                other.setMemberVersion(currentTime);
                // TODO Add OnFailureListener
                this.db.collection(ShawnOrder.COLLECTION_OTHERS).document(user.getGroup()).set(other).addOnSuccessListener(aVoid -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set other in SignUpActivity.");
                });

                Dish dish = new Dish();
                dish.setId(s.toString());
                dish.setDishCode("B01");
                dish.setDishName("Iced Tea®️");
                dish.setCategory("BEVERAGES");
                dish.setGroup(companyCode.getText().toString().trim());
                dish.setPrice(2.50);
                dish.setNumbers(0);
                dish.setUpdateUser(userId.getText().toString().trim());
                dish.setUpdateTime(currentTime);
                dish.setCreateUser(userId.getText().toString().trim());
                dish.setCreateTime(currentTime);

                // TODO Add OnFailureListener
                this.db.collection(ShawnOrder.COLLECTION_DISHES).document(dish.getId()).set(dish).addOnSuccessListener(aVoid -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set dish in SignUpActivity.");
                });

                Table table = new Table();
                table.setId("01");
                table.setGroup(companyCode.getText().toString().trim());
                table.setStatus(Code.TableStatus.STAND_BY.value);
                table.setDishList(Collections.singletonList(dish));
                table.setUpdateUser(userId.getText().toString().trim());
                table.setCreateUser(userId.getText().toString().trim());
                table.setUpdateTime(currentTime);
                table.setCreateTime(currentTime);

                // TODO Add OnFailureListener
                this.db.collection(ShawnOrder.COLLECTION_TABLES).document(table.getGroup() + "_" + table.getId()).set(table).addOnSuccessListener(aVoid -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set table in LoginActivity.");
                });

                // Register account.
                // TODO Add OnFailureListener
                this.db.collection(ShawnOrder.COLLECTION_USERS).document(user.getId()).set(user).addOnSuccessListener(aVoid -> new MaterialAlertDialogBuilder(this.context).setTitle("Successful").setMessage("Now you can login with your account.").setPositiveButton("OK", (dialog, which) -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set user in SignUpActivity.");

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).show());
            });
        }
    }

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.db = FirebaseFirestore.getInstance();

        // Get all the elements from the view.
        Button buttonBack = findViewById(R.id.button_sign_up_back);
        Button buttonSignUp = findViewById(R.id.button_sign_up);
        EditText userId = findViewById(R.id.editText_sign_up_user_id);
        EditText password = findViewById(R.id.editText_sign_up_password);
        EditText companyCode = findViewById(R.id.editText_sign_up_company);

        TextView dishId = findViewById(R.id.textView_sign_up_dish_id);
        dishId.addTextChangedListener(new DishIdTextWatcher(this, this.db));

        this.setGroupCode(companyCode);

        // Set Back button behavior
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Set Sign Up button behavior
        buttonSignUp.setOnClickListener(v -> {
            User user = new User();
            user.setId(userId.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());

            // Check empty
            if (user.checkIsEmpty()) {
                new MaterialAlertDialogBuilder(this).setTitle("Failed").setMessage("User ID, Password could not be empty.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            // Validate User ID
            if (!user.validateUserId()) {
                new MaterialAlertDialogBuilder(this).setTitle("Failed").setMessage("You need to give a 4-16 alphabets or numbers for User ID.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            // Validate Email
//            if (!user.validateEmail()) {
//                new MaterialAlertDialogBuilder(this).setTitle("Failed").setMessage("The Email input is not legal.").setPositiveButton("OK", (dialog, which) -> {
//                }).show();
//                return;
//            }

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            findViewById(R.id.progressBar_sign_up).setVisibility(View.VISIBLE);

            this.db.collection(ShawnOrder.COLLECTION_DISHES).orderBy(Dish.COLUMN_ID, Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get the dish which has the biggest Id in SignUpActivity when Sign Up button is clicked.");

                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    dishId.setText(String.valueOf(Integer.parseInt(Objects.requireNonNull(ds.toObject(Dish.class)).getId()) + 1));
                }
            });
        });
    }

    /**
     *
     */
    public void setGroupCode(EditText companyCode) {
        // Block UI and show progress bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.progressBar_sign_up).setVisibility(View.VISIBLE);

        this.db.collection(ShawnOrder.COLLECTION_OTHERS).get().addOnSuccessListener(queryDocumentSnapshots -> {
            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get other for taking all the group codes in SignUpActivity.");

            Random random = new Random();
            String randomGroupCode;
            List<String> groupList = new ArrayList<>();

            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                groupList.add(Objects.requireNonNull(ds.toObject(Other.class)).getId());
            }

            do {
                randomGroupCode = this.fillOutGroupCode(random.nextInt(9999));
            } while (groupList.contains(randomGroupCode));

            companyCode.setText(randomGroupCode);

            // Release blocking UI and hide progress bar
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            findViewById(R.id.progressBar_sign_up).setVisibility(View.GONE);
        });
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
