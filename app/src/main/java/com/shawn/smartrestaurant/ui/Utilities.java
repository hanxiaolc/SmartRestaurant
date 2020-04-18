package com.shawn.smartrestaurant.ui;

        import java.util.regex.Pattern;

public class Utilities {

    /**
     * Validate if a User ID is legal.
     *
     * @param userId the User ID people input.
     * @return Returns true if the combination of the User ID is legal.
     */
    public static boolean validateUserId(String userId) {
        // alphabets or numbers between 4 to 16 bite.
        String REGEX_PATTERN_USER_ID = "[a-z0-9]{4,16}";
        return Pattern.compile(REGEX_PATTERN_USER_ID).matcher(userId).matches();
    }

    /**
     * Validate if a Company Code is legal.
     *
     * @param companyCode the Company Code people input.
     * @return Returns true if the combination of the Company Code is legal.
     */
    public static boolean validateCompanyCode(String companyCode) {
        // alphabets or numbers between 4 to 8 bite.
        String REGEX_PATTERN_COMPANY = "[a-z0-9]{4,16}";
        return Pattern.compile(REGEX_PATTERN_COMPANY).matcher(companyCode).matches();
    }

    /**
     * Validate if a Company Code is legal.
     *
     * @param email the User ID people input.
     * @return Returns true if the combination of the Company Code is legal.
     */
    public static boolean validateEmail(String email) {
        // Email.
        String REGEX_PATTERN_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        return Pattern.compile(REGEX_PATTERN_EMAIL).matcher(email).matches();
    }
}
