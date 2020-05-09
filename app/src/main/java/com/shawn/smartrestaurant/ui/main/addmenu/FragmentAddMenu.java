package com.shawn.smartrestaurant.ui.main.addmenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import static androidx.core.content.FileProvider.getUriForFile;


/**
 *
 */
public class FragmentAddMenu extends Fragment {

    //
    public static final String ARG_IMAGE_VIEW = "arg_image_view";

    //
    public static final String ARG_HAS_IMAGE = "arg_has_image";

    //
    public static final String ARG_DISH_CODE = "arg_dish_code";

    //
    public static final String ARG_DISH_NAME = "arg_dish_name";

    //
    public static final String ARG_CATEGORY = "arg_category";

    //
    public static final String ARG_PRICE = "arg_price";

    //
    public static final String ARG_ACTION = "arg_action";

    //
    public static final String ACTION_UPDATE = "update";

    //
    public static final String ACTION_ADD = "add";

    //
    private static final int REQUEST_CODE_IMAGE_REQUEST = 101;

    //
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 102;

    //
    private static final int RESULT_CODE_FAILED = 0;

    //
    private ArrayAdapter<String> adapter;

    //
    private ImageView dishImage;

    //
    private Uri tempImageUri;

    //
    private byte[] imageView;

    //
    private String dishCode;

    //
    private String dishName;

    //
    private String category;

    //
    private Double price;

    //
    private boolean hasImage;

    //
    private String action;


    /**
     *
     */
    public FragmentAddMenu() {
    }


    /**
     *
     */
    private static FragmentAddMenu newInstance(byte[] imageView, String dishCode, String dishName, String category, String price, boolean hasImage, String action) {
        FragmentAddMenu fragment = new FragmentAddMenu();
        Bundle args = new Bundle();
        args.putByteArray(ARG_IMAGE_VIEW, imageView);
        args.putString(ARG_DISH_CODE, dishCode);
        args.putString(ARG_DISH_NAME, dishName);
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_PRICE, price);
        args.putBoolean(ARG_HAS_IMAGE, hasImage);
        args.putString(ARG_ACTION, action);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.imageView = getArguments().getByteArray(ARG_IMAGE_VIEW);
            this.dishCode = getArguments().getString(ARG_DISH_CODE);
            this.dishName = getArguments().getString(ARG_DISH_NAME);
            this.category = getArguments().getString(ARG_CATEGORY);
            this.price = getArguments().getDouble(ARG_PRICE);
            this.hasImage = getArguments().getBoolean(ARG_HAS_IMAGE);
            this.action = getArguments().getString(ARG_ACTION);
        }

        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        ((MainActivity) requireActivity()).setCurrentFragment(this);
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_addmenu, container, false);
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the elements of fragment
        dishImage = view.findViewById(R.id.imageView_dish_image);
        TextInputEditText dishName = view.findViewById(R.id.editText_add_menu_dish_name);
        AutoCompleteTextView category = view.findViewById(R.id.autoCompleteTextView_add_menu_category);
        TextInputEditText price = view.findViewById(R.id.editText_add_menu_price);
        Button addToMenu = view.findViewById(R.id.button_add_to_menu);
        if (action.equals(FragmentAddMenu.ACTION_ADD)) {
            addToMenu.setText(R.string.button_add_to_menu);
        } else {
            addToMenu.setText(R.string.button_update);
        }
        ImageButton fetchFromAbulm = view.findViewById(R.id.button_fetch_from_album);
        ImageButton takePhoto = view.findViewById(R.id.button_take_photo);

        // Prepare price dropdown list
        if (null == this.adapter) {
            this.adapter =
                    new ArrayAdapter<>(
                            requireContext(),
                            R.layout.item_list_menu_category,
                            new String[]{"Dish", "Main Food", "Dessert", "Drink", "Flavor"});
        }
        category.setAdapter(this.adapter);

        // Set button behavior
        addToMenu.setOnClickListener(v -> {
            if (Objects.requireNonNull(dishName.getText()).toString().isEmpty() || category.getText().toString().isEmpty() || Objects.requireNonNull(price.getText()).toString().isEmpty()) {
                alertDisplay("Failed", "Dish Name, Category and Price should not be empty.", (dialog, which) -> {
                });
                return;
            }

            User user = ((MainActivity) requireActivity()).getUser();

            Dish dish = new Dish();
            dish.setGroup(user.getGroup());
            dish.setDishName(dishName.getText().toString().trim());
            dish.setCategory(category.getText().toString().trim());
            dish.setPrice(Double.parseDouble(price.getText().toString()));
            dish.setCreateTime(System.currentTimeMillis());
            dish.setUpdateTime(System.currentTimeMillis());
            dish.setCreateUser(user.getId());
            dish.setUpdateUser(user.getId());

            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_DISH_NAME, dish.getDishName()).whereEqualTo(Dish.COLUMN_GROUP, dish.getGroup()).get().addOnCompleteListener(getExistTask -> {
                if (getExistTask.isSuccessful()) {
                    if (!Objects.requireNonNull(getExistTask.getResult()).isEmpty()) {
                        alertDisplay("Failed", "This dish had already existed.", (dialog, which) -> {
                        });
                        return;
                    }

                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).orderBy(Dish.COLUMN_ID, Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(getMaxIdTask -> {
                        if (getMaxIdTask.isSuccessful()) {
                            Dish maxIdDish = Objects.requireNonNull(getMaxIdTask.getResult()).toObjects(Dish.class).get(0);
                            dish.setId(String.valueOf(Integer.parseInt(maxIdDish.getId()) + 1));

                            // Save the new dish
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).document(dish.getId()).set(dish).addOnSuccessListener(aVoid -> {
                            });

                            if (null != dishImage.getDrawable()) {
                                // Save image in Fire Storage
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                ((BitmapDrawable) dishImage.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                StorageReference storageReference = ((MainActivity) requireActivity()).getStorageReference().child(dish.getId() + ".jpg");

                                storageReference.putBytes(stream.toByteArray()).addOnFailureListener(exception -> {
                                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).document(dish.getId()).delete().addOnFailureListener(e -> {
                                        // TODO
                                    });
                                    alertDisplay("Failed", "Adding dish failed with unknown reasons.", (dialog, which) -> {
                                    });
                                }).addOnSuccessListener(taskSnapshot -> {
                                    ((MainActivity) requireActivity()).getMenuNavHostFragment().getNavController().navigate(R.id.action_fragment_nav_addmenu_to_fragment_nav_menu, new Bundle());
                                    ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                                });
                            }

//                            Bundle bundle = new Bundle();
//                            bundle.putString("amount", "textAmount");
                            ((MainActivity) requireActivity()).getMenuNavHostFragment().getNavController().navigate(R.id.action_fragment_nav_addmenu_to_fragment_nav_menu, new Bundle());
                            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                        } else {
                            alertDisplay("Failed", "Adding dish failed with unknown reasons.", (dialog, which) -> {
                            });
                        }
                    });
                } else {
                    alertDisplay("Failed", "Adding dish failed with unknown reasons.", (dialog, which) -> {
                    });
                }
            });
        });

        fetchFromAbulm.setOnClickListener(v -> {
            super.startActivityForResult(new Intent(
                    Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_CODE_IMAGE_REQUEST);
        });

        takePhoto.setOnClickListener(v -> {
            File tempImagePath = requireContext().getFilesDir();
            if (null != Objects.requireNonNull(tempImagePath).listFiles()) {
                for (File f : Objects.requireNonNull(tempImagePath.listFiles())) {
                    if (f.getName().contains("jpg")) {
                        f.delete();
                    }
                }
            }

            File tempImage = new File(tempImagePath, System.currentTimeMillis() + ".jpg");
            tempImageUri = getUriForFile(requireActivity(), "com.shawn.smartrestaurant.fileprovider", tempImage);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);
            super.startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE);
        });
    }

    /**
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CODE_FAILED) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE_REQUEST:
                    Glide.with(this).load(data.getData()).centerCrop().into(dishImage);
                    break;
                case REQUEST_CODE_IMAGE_CAPTURE:
                    Glide.with(this).load(tempImageUri).centerCrop().into(dishImage);
                    break;
            }
        }
    }

    /**
     *
     */
    private void alertDisplay(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", listener);
        AlertDialog alertDialogButton = builder.create();
        alertDialogButton.show();
    }

    /**
     *
     */
    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    /**
     *
     */
    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    /**
     *
     */
    public ImageView getDishImage() {
        return dishImage;
    }

    /**
     *
     */
    public void setDishImage(ImageView dishImage) {
        this.dishImage = dishImage;
    }

    /**
     *
     */
    public Uri getTempImageUri() {
        return tempImageUri;
    }

    /**
     *
     */
    public void setTempImageUri(Uri tempImageUri) {
        this.tempImageUri = tempImageUri;
    }

    /**
     *
     */
    public byte[] getImageView() {
        return imageView;
    }

    /**
     *
     */
    public void setImageView(byte[] imageView) {
        this.imageView = imageView;
    }

    /**
     *
     */
    public String getDishCode() {
        return dishCode;
    }

    /**
     *
     */
    public void setDishCode(String dishCode) {
        this.dishCode = dishCode;
    }

    /**
     *
     */
    public String getDishName() {
        return dishName;
    }

    /**
     *
     */
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    /**
     *
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     */
    public Double getPrice() {
        return price;
    }

    /**
     *
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     *
     */
    public boolean isHasImage() {
        return hasImage;
    }

    /**
     *
     */
    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    /**
     *
     */
    public String getAction() {
        return action;
    }

    /**
     *
     */
    public void setAction(String action) {
        this.action = action;
    }
}
