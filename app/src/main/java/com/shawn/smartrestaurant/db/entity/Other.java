package com.shawn.smartrestaurant.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.regex.Pattern;

@Entity
public class Other {

    //
    public static final String COLUMN_GROUP = "id";

    //
    @NonNull
    @PrimaryKey
    private String id = "";

    //
    @ColumnInfo
    private int menuVersion;

    //
    @ColumnInfo
    private String quantityOfTables;


    /**
     *
     */
    public Other() {
    }

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
    public int getMenuVersion() {
        return menuVersion;
    }

    /**
     *
     */
    public void setMenuVersion(int menuVersion) {
        this.menuVersion = menuVersion;
    }

    /**
     *
     */
    public String getQuantityOfTables() {
        return quantityOfTables;
    }

    /**
     *
     */
    public void setQuantityOfTables(String quantityOfTables) {
        this.quantityOfTables = quantityOfTables;
    }
}
