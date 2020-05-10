package com.shawn.smartrestaurant;

/**
 *
 */
public class Code {

    //
    public static final String INITIAL_DISH_ID = "10000000";

    //
    public static final long ONE_MEGABYTE = 1024 * 1024 * 10;

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
}
