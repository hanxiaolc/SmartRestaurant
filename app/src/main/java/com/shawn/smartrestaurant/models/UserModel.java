package com.shawn.smartrestaurant.models;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserModel {

    //
    public static final String ID = "id";

    //
    public static final String PASSWORD = "password";

    //
    public static final String EMAIL = "email";

    //
    public static final String GROUP = "group";

    //
    public static final String ROLE = "role";

    //
    public static final String STATUS = "status";

    //
    private String userId;

    //
    private String password;

    //
    private String companyCode;

    //
    private String email;

    //
    private boolean isManager;

    //pending, on, off, deleted, outdated.
    private String status;

    /**
     *
     */
    public UserModel() {
    }

    //    public UserModel(String userId, String password, String email) {
//        this.userId = userId;
//        this.password = password;
//        this.email = email;
//    }

    /**
     *
     */
    public UserModel(String userId, String password, String companyCode, String email, boolean isManager) {
        this.userId = userId;
        this.password = password;
        this.companyCode = companyCode;
        this.email = email;
        this.isManager = isManager;
        if (this.isManager) {
            this.status = "on";
        } else {
            this.status = "pending";
        }
    }

    /**
     *
     */
    public boolean isEmptyWithoutCompanyId() {
        if (null == this.userId || null == this.password || null == this.email) {
            return true;
        }

        return this.userId.trim().isEmpty() || this.password.trim().isEmpty() || this.email.trim().isEmpty();
    }

    /**
     *
     */
    public boolean isEmpty() {
        if (null == this.userId || null == this.password || null == this.email || null == this.companyCode) {
            return true;
        }

        return this.userId.trim().isEmpty() || this.password.trim().isEmpty() || this.email.trim().isEmpty() || this.companyCode.trim().isEmpty();
    }

    /**
     *
     */
    public Map toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(ID, this.userId);
        userMap.put(PASSWORD, this.password);
        userMap.put(EMAIL, this.email);
        userMap.put(GROUP, this.companyCode);
        userMap.put(ROLE, this.isManager);

        return userMap;
    }

    /**
     * Validate if a User ID is legal.
     *
     * @return Returns true if the combination of the User ID is legal.
     */
    public boolean validateUserId() {
        // alphabets or numbers between 4 to 16 bite.
        String REGEX_PATTERN_USER_ID = "[a-z0-9]{4,16}";
        return Pattern.compile(REGEX_PATTERN_USER_ID).matcher(this.userId).matches();
    }

    /**
     * Validate if a Company Code is legal.
     *
     * @return Returns true if the combination of the Company Code is legal.
     */
    public boolean validateCompanyCode() {
        // alphabets or numbers between 4 to 8 bite.
        String REGEX_PATTERN_COMPANY = "[a-z0-9]{4,16}";
        return Pattern.compile(REGEX_PATTERN_COMPANY).matcher(this.companyCode).matches();
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

//    /**
//     *
//     */
//    public boolean validateWithoutCompany() {
//        if (validateUserId() && validateEmail()) {
//            return true;
//        }
//
//        return false;
//    }

    /**
     *
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     *
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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
}
