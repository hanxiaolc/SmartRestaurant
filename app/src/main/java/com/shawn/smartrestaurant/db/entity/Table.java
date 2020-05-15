package com.shawn.smartrestaurant.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Entity
public class Table {

    //
    public static final String COLUMN_ID = "id";

    //
    public static final String COLUMN_GROUP = "group";

    //
    @NonNull
    @PrimaryKey
    private String id = "";

    //
    @ColumnInfo
    private String group;

    //
    @ColumnInfo
    private List<Dish> dishList;

    //
    @ColumnInfo
    private String status;

    //
    @ColumnInfo
    private Date startTime;

    //
    @ColumnInfo
    private Date endTime;

    //
    @ColumnInfo
    private Double price;

    //
    @ColumnInfo
    private long createTime;

    //
    @ColumnInfo
    private String createUser;

    //
    @ColumnInfo
    private long updateTime;

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
    public String getStatus() {
        return status;
    }

    /**
     *
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     *
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     *
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     *
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
