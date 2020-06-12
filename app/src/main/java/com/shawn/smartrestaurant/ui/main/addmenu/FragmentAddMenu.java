package com.shawn.smartrestaurant.ui.main.addmenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.entity.Other;
import com.shawn.smartrestaurant.db.entity.Table;
import com.shawn.smartrestaurant.db.entity.User;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 *
 */
public class FragmentAddMenu extends Fragment {

//    //
//    public static final String ARG_IMAGE_VIEW = "arg_image_view";
//
//    //
//    public static final String ARG_HAS_IMAGE = "arg_has_image";

    //
    public static final String ARG_DISH = "arg_dish";

//    //
//    public static final String ARG_DISH_CODE = "arg_dish_code";
//
//    //
//    public static final String ARG_DISH_NAME = "arg_dish_name";
//
//    //
//    public static final String ARG_CATEGORY = "arg_category";
//
//    //
//    public static final String ARG_PRICE = "arg_price";

//    //
//    public static final String ARG_ACTION = "arg_action";

//    //
//    public static final String ACTION_UPDATE = "update";
//
//    //
//    public static final String ACTION_ADD = "add";

//    //
//    private static final int REQUEST_CODE_IMAGE_REQUEST = 101;
//
//    //
//    private static final int REQUEST_CODE_IMAGE_CAPTURE = 102;

//    //
//    private static final int RESULT_CODE_FAILED = 0;

//    //
//    private ImageView dishImage;
//
//    //
//    private Uri tempImageUri;
//
//    //
//    private byte[] imageView;

    //
    private Dish dish;

//    //
//    private String dishCode;
//
//    //
//    private String dishName;
//
//    //
//    private String category;
//
//    //
//    private Double price;

    //
//    private TextView listener;

    //    //
//    private boolean hasImage;
//
//    //
//    private String action;


    /**
     *
     */
    public class AddMenuTextWatcher implements TextWatcher {

        //
        private Dish newDash;

        //
        private View requireView;

        /**
         *
         */
        AddMenuTextWatcher(Dish newDash, View view) {
            this.newDash = newDash;
            this.requireView = view;
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
            if (!Code.READY.equals(s.toString())) {
                return;
            }

            // Save the new dish
            // TODO Add failure handling
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables when new dish number is created in FragmentAddMenu.AddMenuTextWatcher.");

                boolean onService = false;

                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Table table = ds.toObject(Table.class);

                    if (null != Objects.requireNonNull(table).getStartTime() || null != table.getPrice()) {
                        onService = true;
                    }
                }

                if (onService) {
                    // Release blocking UI and hide progress bar
                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    this.requireView.findViewById(R.id.progressBar_add_menu).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Menu information could not be fixed if even single table in ON SERVICE condition.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                // Update menu version to others table.
                long currentTime = System.currentTimeMillis();
                ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).document(this.newDash.getId()).set(this.newDash).addOnSuccessListener(aVoid -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Set new dish in FragmentAddMenu.AddMenuTextWatcher.");

                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).orderBy(Dish.COLUMN_ID).get().addOnSuccessListener(qdsDishes -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get the latest dish list in FragmentAddMenu.AddMenuTextWatcher.");

                        List<Dish> latestDishList = new ArrayList<>();
                        for (DocumentSnapshot ds : qdsDishes.getDocuments()) {
                            latestDishList.add(ds.toObject(Dish.class));
                        }

                        ((MainActivity) requireActivity()).setDishList(latestDishList);
                        ((MainActivity) requireActivity()).getOther().setMenuVersion(currentTime);

                        for (Map.Entry<String, Table> entry : ((MainActivity) requireActivity()).getTableMap().entrySet()) {
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(((MainActivity) requireActivity()).getUser().getGroup() + "_" + entry.getValue().getId()).update(Table.COLUMN_DISH_LIST, latestDishList, Table.COLUMN_UPDATE_USER, ((MainActivity) requireActivity()).getUser().getId(), Table.COLUMN_UPDATE_TIME, currentTime);
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables when new dish number is created in FragmentAddMenu.AddMenuTextWatcher. table id=" + entry.getKey());

                            entry.getValue().setDishList(latestDishList);
                            entry.getValue().setUpdateUser(((MainActivity) requireActivity()).getUser().getId());
                            entry.getValue().setUpdateTime(currentTime);
                        }
                        ((MainActivity) requireActivity()).getOther().setTableVersion(currentTime);

                        // TODO Add failure handling
                        ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(((MainActivity) requireActivity()).getUser().getGroup()).update(Other.COLUMN_MENU_VERSION, currentTime, Other.COLUMN_TABLE_VERSION, currentTime).addOnSuccessListener(aVoidUpdateOther -> {
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other in FragmentAddMenu.AddMenuTextWatcher.");

                            // Release blocking UI and hide progress bar
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            this.requireView.findViewById(R.id.progressBar_add_menu).setVisibility(View.GONE);

                            new MaterialAlertDialogBuilder(requireContext()).setTitle("Successful").setMessage("Menu information have been updated.").setPositiveButton("OK", (dialog, which) -> {
                                ((MainActivity) requireActivity()).getMenuNavHostFragment().getNavController().navigate(R.id.action_framelayout_nav_addmenu_to_framelayout_nav_menu, new Bundle());
                                ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                            }).show();
                        });
                    });
                });
            });

            s.clear();
        }
    }

    /**
     *
     */
    public FragmentAddMenu() {
    }


//    /**
//     *
//     */
//    private static FragmentAddMenu newInstance(byte[] imageView, String dishCode, String dishName, String category, String price, boolean hasImage, String action) {
//        FragmentAddMenu fragment = new FragmentAddMenu();
//        Bundle args = new Bundle();
////        args.putByteArray(ARG_IMAGE_VIEW, imageView);
//        args.putString(ARG_DISH_CODE, dishCode);
//        args.putString(ARG_DISH_NAME, dishName);
//        args.putString(ARG_CATEGORY, category);
//        args.putString(ARG_PRICE, price);
////        args.putBoolean(ARG_HAS_IMAGE, hasImage);
////        args.putString(ARG_ACTION, action);
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) requireActivity()).authenticate();

        if (getArguments() != null) {
            this.dish = new Gson().fromJson(getArguments().getString(FragmentAddMenu.ARG_DISH), Dish.class);
//            this.imageView = getArguments().getByteArray(ARG_IMAGE_VIEW);
//            this.dishCode = getArguments().getString(ARG_DISH_CODE);
//            this.dishName = getArguments().getString(ARG_DISH_NAME);
//            this.category = getArguments().getString(ARG_CATEGORY);
//            this.price = getArguments().getDouble(ARG_PRICE);
//            this.dishId = getArguments().getString(ARG_DISH_ID);
//            this.hasImage = getArguments().getBoolean(ARG_HAS_IMAGE);
//            this.action = getArguments().getString(ARG_ACTION);
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
        ((AppBarLayout) requireActivity().findViewById(R.id.appBarLayout_main)).setExpanded(true);

        View view = inflater.inflate(R.layout.framelayout_nav_addmenu, container, false);

        TextInputEditText dishCode = view.findViewById(R.id.editText_add_menu_dish_code);
        TextInputEditText dishName = view.findViewById(R.id.editText_add_menu_dish_name);
        AutoCompleteTextView category = view.findViewById(R.id.autoCompleteTextView_add_menu_category);
        TextInputEditText price = view.findViewById(R.id.editText_add_menu_price);
        TextView listener = new TextView(requireContext());

        Button addNew = view.findViewById(R.id.button_add_to_menu);
        Button delete = view.findViewById(R.id.button_add_menu_delete);

        long currentTime = System.currentTimeMillis();

        if (null != getArguments()) {
            addNew.setText(R.string.button_update);

            if (!((MainActivity) requireActivity()).getUser().isManager()) {
                addNew.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                dishCode.setFocusable(false);
                dishName.setFocusable(false);
                category.setFocusable(false);
                category.setAdapter(null);
                price.setFocusable(false);
            }
        } else {
            delete.setVisibility(View.GONE);
        }

        // Set button behavior
        addNew.setOnClickListener(v -> {
            if (Objects.requireNonNull(dishName.getText()).toString().isEmpty() || category.getText().toString().isEmpty() || Objects.requireNonNull(price.getText()).toString().isEmpty()) {
                new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Dish Name, Category and Price should not be empty.").setPositiveButton("OK", (dialog, which) -> {
                }).show();
                return;
            }

            User user = ((MainActivity) requireActivity()).getUser();

            if (null == this.dish) {
                this.dish = new Dish();
                this.dish.setGroup(user.getGroup());
                this.dish.setCreateTime(currentTime);
                this.dish.setUpdateTime(currentTime);
                this.dish.setCreateUser(user.getId());
                this.dish.setUpdateUser(user.getId());
            }

            if (null != dishCode.getText() && !dishCode.getText().toString().isEmpty()) {
                this.dish.setDishCode(Objects.requireNonNull(dishCode.getText()).toString().trim());
            }
            this.dish.setDishName(dishName.getText().toString().trim());
            this.dish.setCategory(category.getText().toString().trim());
            this.dish.setPrice(Double.parseDouble(price.getText().toString()));
            this.dish.setUpdateTime(currentTime);
            this.dish.setUpdateUser(user.getId());

            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_add_menu).setVisibility(View.VISIBLE);

            // TODO Add failure handling
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_DISH_NAME, dish.getDishName()).whereEqualTo(Dish.COLUMN_GROUP, dish.getGroup()).get().addOnSuccessListener(dsByName -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get dishes when addNew button is clicked for checking duplicate in FragmentAddMenu.");

                if (!Objects.requireNonNull(dsByName).isEmpty() && ((this.dish.getId().isEmpty() || !(this.dish.getId().equals(Objects.requireNonNull(dsByName.getDocuments().get(0).toObject(Dish.class)).getId()))))) {
                    // Release blocking UI and hide progress bar
                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    requireView().findViewById(R.id.progressBar_add_menu).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("This dish had already existed.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                if (!this.dish.getId().isEmpty()) {
                    listener.addTextChangedListener(new AddMenuTextWatcher(this.dish, requireView()));
                    listener.setText(Code.READY);
                } else {
                    // TODO Add failure handling
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).orderBy(Dish.COLUMN_ID, Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(dsById -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get dishes when addNew button is clicked for getting the biggest dish number in FragmentAddMenu.");

                        this.dish.setId(String.valueOf(Integer.parseInt(Objects.requireNonNull(dsById.getDocuments().get(0).toObject(Dish.class)).getId()) + 1));
                        listener.addTextChangedListener(new AddMenuTextWatcher(this.dish, requireView()));
                        listener.setText(Code.READY);
                    });
                }
            });
        });

        delete.setOnClickListener(v -> {
            // Block UI and show progress bar
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requireView().findViewById(R.id.progressBar_add_menu).setVisibility(View.VISIBLE);

            // TODO Add failure handling
            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).whereEqualTo(Table.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables when DELETE button is clicked in FragmentAddMenu.");

                boolean onService = false;

                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Table table = ds.toObject(Table.class);

                    if (null != Objects.requireNonNull(table).getStartTime() || null != table.getPrice()) {
                        onService = true;
                    }
                }

                if (onService) {
                    // Release blocking UI and hide progress bar
                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    requireView().findViewById(R.id.progressBar_add_menu).setVisibility(View.GONE);

                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Menu information could not be fixed if even single table in ON SERVICE condition.").setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                    return;
                }

                ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).document(this.dish.getId()).delete().addOnSuccessListener(aVoid -> {
                    MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get dishes when delete button is clicked in FragmentAddMenu.");


                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_GROUP, ((MainActivity) requireActivity()).getUser().getGroup()).orderBy(Dish.COLUMN_ID).get().addOnSuccessListener(qdsDishes -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update tables when delete button is clicked in FragmentAddMenu.");

                        List<Dish> latestDishList = new ArrayList<>();
                        for (DocumentSnapshot ds : qdsDishes.getDocuments()) {
                            latestDishList.add(ds.toObject(Dish.class));
                        }

                        ((MainActivity) requireActivity()).setDishList(latestDishList);
                        ((MainActivity) requireActivity()).getOther().setMenuVersion(currentTime);

                        for (Map.Entry<String, Table> entry : ((MainActivity) requireActivity()).getTableMap().entrySet()) {
                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_TABLES).document(((MainActivity) requireActivity()).getUser().getGroup() + "_" + entry.getValue().getId()).update(Table.COLUMN_DISH_LIST, latestDishList, Table.COLUMN_UPDATE_USER, ((MainActivity) requireActivity()).getUser().getId(), Table.COLUMN_UPDATE_TIME, currentTime);
                            MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Get tables when new dish number is created in FragmentAddMenu.AddMenuTextWatcher. table id=" + entry.getKey());

                            entry.getValue().setDishList(latestDishList);
                            entry.getValue().setUpdateUser(((MainActivity) requireActivity()).getUser().getId());
                            entry.getValue().setUpdateTime(currentTime);
                        }
                    });

                    // Update menu version to others table.
                    // TODO Add failure handling
                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(((MainActivity) requireActivity()).getUser().getGroup()).update(Other.COLUMN_MENU_VERSION, currentTime, Other.COLUMN_TABLE_VERSION, currentTime).addOnSuccessListener(aVoidUpdateOther -> {
                        MainActivity.debug(Code.LOG_DB_DEBUG_TAG, "Update other when delete button is clicked in FragmentAddMenu.");

                        // Release blocking UI and hide progress bar
                        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        requireView().findViewById(R.id.progressBar_add_menu).setVisibility(View.GONE);

                        new MaterialAlertDialogBuilder(requireContext()).setTitle("Successful").setMessage("Menu information have been updated.").setPositiveButton("OK", (dialog, which) -> {
                            ((MainActivity) requireActivity()).getMenuNavHostFragment().getNavController().navigate(R.id.action_framelayout_nav_addmenu_to_framelayout_nav_menu, null);
                            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                        }).show();
                    });
                });
            });
        });

        return view;
    }

    /**
     *
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the elements of fragment
//        dishImage = view.findViewById(R.id.imageView_dish_image);
        TextInputEditText dishCode = view.findViewById(R.id.editText_add_menu_dish_code);
        TextInputEditText dishName = view.findViewById(R.id.editText_add_menu_dish_name);
        AutoCompleteTextView category = view.findViewById(R.id.autoCompleteTextView_add_menu_category);
        TextInputEditText price = view.findViewById(R.id.editText_add_menu_price);

        if (null != getArguments()) {
            dishCode.setText(this.dish.getDishCode());
            dishName.setText(this.dish.getDishName());
            category.setText(this.dish.getCategory());
            price.setText(String.valueOf(this.dish.getPrice()));
        }
//        ImageButton fetchFromAbulm = view.findViewById(R.id.button_fetch_from_album);
//        ImageButton takePhoto = view.findViewById(R.id.button_take_photo);

        // Prepare price dropdown list
        Set<String> categorySet = new HashSet<>();
        for (Dish dish : ((MainActivity) requireActivity()).getDishList()) {
            categorySet.add(dish.getCategory());
        }

        category.setAdapter(new ArrayAdapter<>(
                requireContext(),
                R.layout.item_list_menu_category,
                new ArrayList<>(categorySet)));

        if (!((MainActivity) requireActivity()).getUser().isManager()) {
            category.setAdapter(null);
        }

        // Set button behavior
//        addToMenu.setOnClickListener(v -> {
//            if (Objects.requireNonNull(dishName.getText()).toString().isEmpty() || category.getText().toString().isEmpty() || Objects.requireNonNull(price.getText()).toString().isEmpty()) {
//                new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Dish Name, Category and Price should not be empty.").setPositiveButton("OK", (dialog, which) -> {
//                }).show();
//                return;
//            }
//
//            User user = ((MainActivity) requireActivity()).getUser();
//
//            Dish dish = new Dish();
//            dish.setGroup(user.getGroup());
//            dish.setDishCode(Objects.requireNonNull(dishCode.getText()).toString().trim());
//            dish.setDishName(dishName.getText().toString().trim());
//            dish.setCategory(category.getText().toString().trim());
//            dish.setPrice(Double.parseDouble(price.getText().toString()));
//            dish.setCreateTime(System.currentTimeMillis());
//            dish.setUpdateTime(System.currentTimeMillis());
//            dish.setCreateUser(user.getId());
//            dish.setUpdateUser(user.getId());
//            if (null != dishImage.getDrawable()) {
//                dish.setHasImage(true);
//            } else {
//                dish.setHasImage(false);
//            }
//
//            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).whereEqualTo(Dish.COLUMN_DISH_NAME, dish.getDishName()).whereEqualTo(Dish.COLUMN_GROUP, dish.getGroup()).get().addOnCompleteListener(getExistTask -> {
//                if (getExistTask.isSuccessful()) {
//                    if (!Objects.requireNonNull(getExistTask.getResult()).isEmpty()) {
//                        new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("This dish had already existed.").setPositiveButton("OK", (dialog, which) -> {
//                        }).show();
//                        return;
//                    }
//
//                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).orderBy(Dish.COLUMN_ID, Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(getMaxIdTask -> {
//                        if (getMaxIdTask.isSuccessful()) {
//                            Dish maxIdDish = Objects.requireNonNull(getMaxIdTask.getResult()).toObjects(Dish.class).get(0);
//                            dish.setId(String.valueOf(Integer.parseInt(maxIdDish.getId()) + 1));
//
//                            // Save the new dish
//                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).document(dish.getId()).set(dish).addOnSuccessListener(aVoid -> {
//                            });
//
//                            if (null != dishImage.getDrawable()) {
//                                // Save image in Fire Storage
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//                                ((BitmapDrawable) dishImage.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                                StorageReference storageReference = ((MainActivity) requireActivity()).getStorageReference().child(dish.getId() + ".jpg");
//
//                                storageReference.putBytes(stream.toByteArray()).addOnFailureListener(exception -> {
//                                    ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_DISHES).document(dish.getId()).delete().addOnFailureListener(e -> {
//                                        // TODO
//                                    });
//                                    alertDisplay("Failed", "Adding dish failed with unknown reasons.", (dialog, which) -> {
//                                    });
//                                }).addOnSuccessListener(taskSnapshot -> {
//                                    ((MainActivity) requireActivity()).getMenuNavHostFragment().getNavController().navigate(R.id.action_fragment_nav_addmenu_to_fragment_nav_menu, new Bundle());
//                                    ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
//                                });
//                            }
//
//                            // Update menu version to others table.
//                            // TODO Add failure handling
//                            ((MainActivity) requireActivity()).getDb().collection(ShawnOrder.COLLECTION_OTHERS).document(user.getGroup()).update(Other.COLUMN_MENU_VERSION, System.currentTimeMillis());
//
//                            ((MainActivity) requireActivity()).getMenuNavHostFragment().getNavController().navigate(R.id.action_framelayout_nav_addmenu_to_framelayout_nav_menu, new Bundle());
//                            ((MainActivity) requireActivity()).getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
//                        } else {
//                            new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Adding dish failed with unknown reasons.").setPositiveButton("OK", (dialog, which) -> {
//                            }).show();
//                        }
//                    });
//                } else {
//                    new MaterialAlertDialogBuilder(requireContext()).setTitle("Failed").setMessage("Adding dish failed with unknown reasons.").setPositiveButton("OK", (dialog, which) -> {
//                    }).show();
//                }
//            });
//        });
//
//        fetchFromAbulm.setOnClickListener(v -> {
//            super.startActivityForResult(new Intent(
//                    Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_CODE_IMAGE_REQUEST);
//        });
//
//        takePhoto.setOnClickListener(v -> {
//            File tempImagePath = requireContext().getFilesDir();
//
//            File tempImage = new File(tempImagePath, System.currentTimeMillis() + ".jpg");
//            tempImageUri = getUriForFile(requireActivity(), "com.shawn.smartrestaurant.fileprovider", tempImage);
//
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);
//            super.startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE);
//        });
    }

//    /**
//     *
//     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_CODE_FAILED) {
//            switch (requestCode) {
//                case REQUEST_CODE_IMAGE_REQUEST:
//                    Glide.with(this).load(data.getData()).centerCrop().into(dishImage);
//                    break;
//                case REQUEST_CODE_IMAGE_CAPTURE:
//                    Glide.with(this).load(tempImageUri).centerCrop().into(dishImage);
//                    break;
//            }
//        }
//    }

//    /**
//     *
//     */
//    private void alertDisplay(String title, String message, DialogInterface.OnClickListener listener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton("OK", listener);
//        AlertDialog alertDialogButton = builder.create();
//        alertDialogButton.show();
//    }

//    /**
//     *
//     */
//    public ImageView getDishImage() {
//        return dishImage;
//    }
//
//    /**
//     *
//     */
//    public void setDishImage(ImageView dishImage) {
//        this.dishImage = dishImage;
//    }
//
//    /**
//     *
//     */
//    public Uri getTempImageUri() {
//        return tempImageUri;
//    }
//
//    /**
//     *
//     */
//    public void setTempImageUri(Uri tempImageUri) {
//        this.tempImageUri = tempImageUri;
//    }
//
//    /**
//     *
//     */
//    public byte[] getImageView() {
//        return imageView;
//    }
//
//    /**
//     *
//     */
//    public void setImageView(byte[] imageView) {
//        this.imageView = imageView;
//    }
//
//    /**
//     *
//     */
//    public String getDishCode() {
//        return dishCode;
//    }
//
//    /**
//     *
//     */
//    public void setDishCode(String dishCode) {
//        this.dishCode = dishCode;
//    }
//
//    /**
//     *
//     */
//    public String getDishName() {
//        return dishName;
//    }
//
//    /**
//     *
//     */
//    public void setDishName(String dishName) {
//        this.dishName = dishName;
//    }
//
//    /**
//     *
//     */
//    public String getCategory() {
//        return category;
//    }
//
//    /**
//     *
//     */
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    /**
//     *
//     */
//    public Double getPrice() {
//        return price;
//    }
//
//    /**
//     *
//     */
//    public void setPrice(Double price) {
//        this.price = price;
//    }

//    /**
//     *
//     */
//    public boolean isHasImage() {
//        return hasImage;
//    }
//
//    /**
//     *
//     */
//    public void setHasImage(boolean hasImage) {
//        this.hasImage = hasImage;
//    }
//
//    /**
//     *
//     */
//    public String getAction() {
//        return action;
//    }
//
//    /**
//     *
//     */
//    public void setAction(String action) {
//        this.action = action;
//    }
}
