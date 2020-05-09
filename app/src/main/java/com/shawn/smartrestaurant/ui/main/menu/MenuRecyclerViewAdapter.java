package com.shawn.smartrestaurant.ui.main.menu;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.shawn.smartrestaurant.ui.main.addmenu.FragmentAddMenu;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter {

    //
    private List<Dish> dishList;

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
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
        View itemViewTemp = null;

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
            dishName.setText(dish.getDishName());
            dishPrice.setText(String.valueOf(dish.getPrice()));

            if (dish.isHasImage()) {
                StorageReference storageReferenceDishes = FirebaseStorage.getInstance().getReference().child(ShawnOrder.COLLECTION_DISHES);
                // TODO Add OnFailureListener
                storageReferenceDishes.child(dish.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(holder.itemView).load(uri).into(imageView);
                });
            } else {
                imageView.setImageResource(R.drawable.ic_crop_original_black_24dp);
            }

            holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();

                // Transfer imageView to be a byte array
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] imageInByte = stream.toByteArray();

                bundle.putByteArray(FragmentAddMenu.ARG_IMAGE_VIEW, imageInByte);
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
}
