package com.shawn.smartrestaurant.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;
import java.util.regex.Pattern;

@Entity
public class Other {

    //
    public static final String COLUMN_GROUP = "id";

    //
    public static final String COLUMN_MENU_VERSION = "menuVersion";

    //
    public static final String COLUMN_TABLE_VERSION = "tableVersion";

    //
    public static final String COLUMN_MEMBER_VERSION = "memberVersion";

    //
    public static final String COLUMN_HISTORY_VERSION = "historyVersion";

    //
//    public static final String COLUMN_QUANTITY_OF_TABLES = "quantityOfTables";

    //
    @NonNull
    @PrimaryKey
    private String id = "";

    //
    @ColumnInfo
    private long menuVersion;

    //
    @ColumnInfo
    private long tableVersion;

    //
    @ColumnInfo
    private long memberVersion;

    //
    @ColumnInfo
    private long historyVersion;

    //
//    @ColumnInfo
//    private int quantityOfTables;


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
    public long getMenuVersion() {
        return menuVersion;
    }

    /**
     *
     */
    public void setMenuVersion(long menuVersion) {
        this.menuVersion = menuVersion;
    }

    /**
     *
     */
    public long getTableVersion() {
        return tableVersion;
    }

    /**
     *
     */
    public void setTableVersion(long tableVersion) {
        this.tableVersion = tableVersion;
    }

    /**
     *
     */
    public long getMemberVersion() {
        return memberVersion;
    }

    /**
     *
     */
    public void setMemberVersion(long memberVersion) {
        this.memberVersion = memberVersion;
    }

    /**
     *
     */
    public long getHistoryVersion() {
        return historyVersion;
    }

    /**
     *
     */
    public void setHistoryVersion(long historyVersion) {
        this.historyVersion = historyVersion;
    }

    //    /**
//     *
//     */
//    public String getQuantityOfTables() {
//        return quantityOfTables;
//    }
//
//    /**
//     *
//     */
//    public void setQuantityOfTables(String quantityOfTables) {
//        this.quantityOfTables = quantityOfTables;
//    }
}
