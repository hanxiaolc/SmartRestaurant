package com.shawn.smartrestaurant.ui.main.menu;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shawn.smartrestaurant.Code;
import com.shawn.smartrestaurant.R;
import com.shawn.smartrestaurant.db.entity.Dish;
import com.shawn.smartrestaurant.db.firebase.ShawnOrder;
import com.shawn.smartrestaurant.ui.main.MainActivity;
import com.shawn.smartrestaurant.ui.main.addmenu.FragmentAddMenu;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<Dish> dishList;

    //
    private Map<String, Bitmap> menuImagesMap;

    //
    private StorageReference storageReferenceDishes;

    /**
     *
     */
    public static class MenuRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        //
        private View itemView;

        //
        private int itemViewType;

        /**
         *
         */
        MenuRecyclerViewAdapterViewHolder(View itemView, int itemViewType) {
            super(itemView);

            this.itemView = itemView;
            this.itemViewType = itemViewType;
        }
    }

    /**
     *
     */
    MenuRecyclerViewAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    /**
     *
     */
    MenuRecyclerViewAdapter(List<Dish> dishList, Map<String, Bitmap> menuImagesMap) {
        this.dishList = dishList;
        this.menuImagesMap = menuImagesMap;
    }

    /**
     *
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
        View itemViewTemp;

        if (itemViewType == (Code.MenuRecyclerViewType.HEADER.id)) {
            itemViewTemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_group_title_menu, parent, false);
        } else {
            itemViewTemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_menu, parent, false);
        }

        return new MenuRecyclerViewAdapterViewHolder(itemViewTemp, itemViewType);
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Dish dish = this.dishList.get(position);

        if (Code.MenuRecyclerViewType.HEADER.id == holder.getItemViewType()) {
            TextView groupTitle = holder.itemView.findViewById(R.id.textView_menu_group_title);
            groupTitle.setText(dish.getCategory());
        } else {
            TextView dishCode = holder.itemView.findViewById(R.id.textView_menu_dish_code);
            TextView dishName = holder.itemView.findViewById(R.id.textView_menu_dish_name);
            TextView dishPrice = holder.itemView.findViewById(R.id.textView_menu_dish_price);
            ImageView imageView = holder.itemView.findViewById(R.id.imageView_menu_image);
            dishCode.setText(dish.getDishCode());
            if (dish.getDishCode().isEmpty()) {
                dishCode.setVisibility(View.GONE);
            }
            dishName.setText(dish.getDishName());
            dishPrice.setText("$" + dish.getPrice());

            if (dish.isHasImage()) {
                if (null != this.menuImagesMap.get(dish.getId())) {
                    imageView.setImageBitmap(this.menuImagesMap.get(dish.getId()));
                } else {
                    // TODO Add OnFailureListener
                    FirebaseStorage.getInstance().getReference().child(ShawnOrder.COLLECTION_DISHES).child(dish.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(holder.itemView).load(uri).into(imageView);
                    });
                }
            } else {
                imageView.setImageResource(R.drawable.ic_crop_original_black_24dp);
            }

            holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();

                if (dish.isHasImage()) {
                    // Transfer imageView to be a byte array
                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byte[] imageInByte = stream.toByteArray();

                    bundle.putByteArray(FragmentAddMenu.ARG_IMAGE_VIEW, imageInByte);
                }

                bundle.putBoolean(FragmentAddMenu.ARG_HAS_IMAGE, dish.isHasImage());
                bundle.putString(FragmentAddMenu.ARG_DISH_CODE, dish.getDishCode());
                bundle.putString(FragmentAddMenu.ARG_DISH_NAME, dish.getDishName());
                bundle.putString(FragmentAddMenu.ARG_CATEGORY, dish.getCategory());
                bundle.putDouble(FragmentAddMenu.ARG_PRICE, dish.getPrice());
                bundle.putString(FragmentAddMenu.ARG_ACTION, FragmentAddMenu.ACTION_UPDATE);

                Navigation.findNavController(view)
                        .navigate(R.id.action_fragment_nav_menu_to_fragment_nav_addmenu, bundle);
            });
        }
    }

    /**
     *
     */
    @Override
    public int getItemCount() {
        return dishList.size();
    }

    /**
     *
     */
    @Override
    public int getItemViewType(int position) {
        return null == dishList.get(position).getDishName() ?
                Code.MenuRecyclerViewType.HEADER.id : Code.MenuRecyclerViewType.ITEM.id;
    }

    /**
     *
     */
    public List<Dish> getDishList() {
        return dishList;
    }

    /**
     *
     */
    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    /**
     *
     */
    public Map<String, Bitmap> getMenuImagesMap() {
        return menuImagesMap;
    }

    /**
     *
     */
    public void setMenuImagesMap(Map<String, Bitmap> menuImagesMap) {
        this.menuImagesMap = menuImagesMap;
    }
}
