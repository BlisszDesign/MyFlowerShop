package com.example.android.myflowershop.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class FlowersContract {
    private FlowersContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.myflowers";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FLOWERS = "flowers";

    public static final class FlowersEntry implements BaseColumns {
        /** The content URI to access the flowers data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FLOWERS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of flowers.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLOWERS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single flower.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLOWERS;

        //        table name
        public final static String TABLE_NAME = "flowers";
        //        id number, type INTEGER
        public final static String _ID = BaseColumns._ID;
        /**
         * Type of the flowers.
         *
         * The only possible values are {@link #TYPE_CALLA}, {@link #TYPE_ROSE},
         * {@link #TYPE_ORCHIDS}, {@link #TYPE_WEDDING} or {@link #TYPE_ROMANTIC}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_FLOWERS_TYPE = "flowers_type";
        //       type TEXT
        public final static String COLUMN_PRODUCT_NAME = "product_name";
        //      type TEXT
        public final static String COLUMN_PRODUCT_DESCRIPTION = "description";
        //       type INTEGER
        public final static String COLUMN_PRICE = "price";
        //       type INTEGER
        public final static String COLUMN_QUANTITY = "quantity";
        //       type TEXT
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        //       type TEXT
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER= "supplier_phone_number";

        /**
         * Possible values for the type of the flowers.
         */
        public static final int TYPE_CALLA = 0;
        public static final int TYPE_ROSE = 1;
        public static final int TYPE_ORCHIDS = 2;
        public static final int TYPE_WEDDING = 3;
        public static final int TYPE_ROMANTIC = 4;

        /**
         * Returns whether or not the given type is {@link #TYPE_CALLA}, {@link #TYPE_ROSE},
         * {@link #TYPE_ORCHIDS}, {@link #TYPE_WEDDING} or {@link #TYPE_ROMANTIC}.
         */
        public static boolean isValidType(int type) {
            if (type == TYPE_CALLA || type == TYPE_ROSE || type == TYPE_ORCHIDS
                    || type == TYPE_WEDDING || type == TYPE_ROMANTIC) {
                return true;
            }
            return false;
        }
    }
}
