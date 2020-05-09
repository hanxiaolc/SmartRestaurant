package com.shawn.smartrestaurant.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 *
 */
@Entity
public class Dish {

    //
    public static final String COLUMN_ID = "id";

    //
    public static final String COLUMN_DISH_NAME = "dishName";

    //
    public static final String COLUMN_GROUP = "group";

    //
    @NonNull
    @PrimaryKey
    private String id = "";

    //
    @ColumnInfo
    private String dishCode;

    //
    @ColumnInfo
    private String dishName;

    //
    @ColumnInfo
    private String group;

    //
    @ColumnInfo
    private String category;

    //
    @ColumnInfo
    private Double price;

    //
    @ColumnInfo
    private int numbers;

    //
    @ColumnInfo
    private boolean hasImage;

    //
    @ColumnInfo
    private int timeConsumption;

    //
    @ColumnInfo
    private long createTime;

    //
    @ColumnInfo
    private long updateTime;

    //
    @ColumnInfo
    private String createUser;

    //
    @ColumnInfo
    private String updateUser;


    /**
     *
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     *
     */
    public void setId(@NonNull String id) {
        this.id = id;
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
    public String getGroup() {
        return group;
    }

    /**
     *
     */
    public void setGroup(String group) {
        this.group = group;
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
    public int getNumbers() {
        return numbers;
    }

    /**
     *
     */
    public void setNumbers(int numbers) {
        this.numbers = numbers;
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
    public int getTimeConsumption() {
        return timeConsumption;
    }

    /**
     *
     */
    public void setTimeConsumption(int timeConsumption) {
        this.timeConsumption = timeConsumption;
    }

    /**
     *
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     *
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     *
     */
    public long getUpdateTime() {
        return updateTime;
    }

    /**
     *
     */
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     *
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     *
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     *
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     *
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
