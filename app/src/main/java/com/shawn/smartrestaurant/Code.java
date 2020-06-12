package com.shawn.smartrestaurant;

/**
 *
 */
public class Code {

    //
    public static final String INITIAL_DISH_ID = "10000000";

    //
    public static final int MAX_NUMBER_OF_TABLES = 10;

    //
    public static final String READY = "ready";

    //
    public static final String LOG_DB_DEBUG_TAG = "HANXIAOLI(DB): ";

    //
    public static final boolean IS_DEBUG_MODE = false;

    //
    private static final long ONE_MEGABYTE = 1024 * 1024 * 10;

    //
    public static final long TEN_MEGABYTE = ONE_MEGABYTE * 10;

    /**
     *
     */
    public enum DishCategory {

        /**
         *
         */
        DISH(1, "Dish"), MAIN_FOOD(2, "Main Food"), DRINK(3, "Drink"), DESSERT(4, "Dessert"), FLAVOR(5, "Flavor");

        //
        public int id;

        //
        public String value;

        /**
         *
         */
        DishCategory(int id, String value) {
            this.id = id;
            this.value = value;
        }

        /**
         *
         */
        public static int getId(String value) {
            for (DishCategory dc : DishCategory.values()) {
                if (dc.value.equals(value)) {
                    return dc.id;
                }
            }
            return -1;
        }
    }

    /**
     *
     */
    public enum MenuRecyclerViewType {

        /**
         *
         */
        HEADER(1, "header"), ITEM(0, "item");

        //
        public int id;

        //
        public String value;

        /**
         *
         */
        MenuRecyclerViewType(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    /**
     *
     */
    public enum DataStatus {

        /**
         *
         */
        LATEST(0, "latest"), UPDATED(1, "updated");

        //
        public int id;

        //
        public String value;

        /**
         *
         */
        DataStatus(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    /**
     *
     */
    public enum TableStatus {

        /**
         *
         */
        READY(0, "READY"), ON_SERVICE(1, "ON SERVICE"), CLEAN_UP(3, "CLEANUP"), STAND_BY(4, "STANDBY");

        //
        public int id;

        //
        public String value;

        /**
         *
         */
        TableStatus(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    /**
     *
     */
    public enum ActionBarTitle {

        /**
         *
         */
        DEFAULT(0, "SMART ORDER"), TABLE(1, "TABLE");

        //
        public int id;

        //
        public String value;

        /**
         *
         */
        ActionBarTitle(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }
}
