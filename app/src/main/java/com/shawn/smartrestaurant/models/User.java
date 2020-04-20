package com.shawn.smartrestaurant.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Entity
public class User {

//    //
//    public static final String ID = "id";
//
//    //
//    public static final String PASSWORD = "password";
//
//    //
//    public static final String EMAIL = "email";
//
//    //
//    public static final String GROUP = "group";
//
//    //
//    public static final String ROLE = "role";
//
//    //
//    public static final String STATUS = "status";
//
//    //
//    public static final String CREATE_TIME = "create_time";
//
//    //
//    public static final String UPDATE_TIME = "update_time";

    //
    @PrimaryKey
    private String id;

    //
    @ColumnInfo
    private String password;

    //
    @ColumnInfo
    private String group;

    //
    @ColumnInfo
    private String email;

    //
    @ColumnInfo
    private boolean isManager;

    //pending, on, off, deleted, outdated.
    @ColumnInfo
    private String status;

    //
    @ColumnInfo
    private long createTime;

    //
    @ColumnInfo
    private long updateTime;

    /**
     *
     */
    public User() {
    }

    /**
     *
     */
    public User(String id, String password, String group, String email, boolean isManager) {
        this.id = id;
        this.password = password;
        this.group = group;
        this.email = email;
        this.isManager = isManager;
        if (this.isManager) {
            this.status = "on";
        } else {
            this.status = "pending";
        }
        this.createTime = new Date().getTime();
        this.updateTime = new Date().getTime();
    }

//    /**
//     *
//     */
//    public boolean isEmptyWithoutCompanyId() {
//        if (null == this.userId || null == this.password || null == this.email) {
//            return true;
//        }
//
//        return this.userId.trim().isEmpty() || this.password.trim().isEmpty() || this.email.trim().isEmpty();
//    }

    /**
     *
     */
    public boolean checkIsEmpty() {
        if (null == this.id || null == this.password || null == this.email || null == this.group) {
            return true;
        }

        return this.id.trim().isEmpty() || this.password.trim().isEmpty() || this.email.trim().isEmpty() || this.group.trim().isEmpty();
    }

//    /**
//     *
//     */
//    public Map toMap() {
//        Map<String, Object> userMap = new HashMap<>();
//
//        userMap.put(ID, this.userId);
//        userMap.put(PASSWORD, this.password);
//        userMap.put(EMAIL, this.email);
//        userMap.put(GROUP, this.companyCode);
//        userMap.put(ROLE, this.isManager);
//        userMap.put(STATUS, this.status);
//        userMap.put(CREATE_TIME, this.createTime);
//        userMap.put(UPDATE_TIME, this.updateTime);
//
//        return userMap;
//    }

    /**
     * Validate if a User ID is legal.
     *
     * @return Returns true if the combination of the User ID is legal.
     */
    public boolean validateUserId() {
        // alphabets or numbers between 4 to 16 bite.
        String REGEX_PATTERN_USER_ID = "[a-z0-9]{4,16}";
        return Pattern.compile(REGEX_PATTERN_USER_ID).matcher(this.id).matches();
    }

    /**
     * Validate if a Company Code is legal.
     *
     * @return Returns true if the combination of the Company Code is legal.
     */
    public boolean validateGroup() {
        // alphabets or numbers between 4 to 8 bite.
        String REGEX_PATTERN_COMPANY = "[0-9]{4,4}";
        return Pattern.compile(REGEX_PATTERN_COMPANY).matcher(this.group).matches();
    }

    /**
     * Validate if a Company Code is legal.
     *
     * @return Returns true if the combination of the Company Code is legal.
     */
    public boolean validateEmail() {
        // Email.
        String REGEX_PATTERN_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        return Pattern.compile(REGEX_PATTERN_EMAIL).matcher(email).matches();
    }

    /**
     *
     */
    public String getId() {
        return id;
    }

    /**
     *
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     */
    public void setPassword(String password) {
        this.password = password;
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
    public String getEmail() {
        return email;
    }

    /**
     *
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     */
    public boolean isManager() {
        return isManager;
    }

    /**
     *
     */
    public void setManager(boolean isManager) {
        this.isManager = isManager;
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
}
