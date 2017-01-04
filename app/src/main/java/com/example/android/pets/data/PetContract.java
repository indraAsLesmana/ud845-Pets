package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by indraaguslesmana on 12/30/16.
 */

public final class PetContract {

    /**
     * Content AUTHOTIRY
     * */
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    /**
     * Base Content URI
     * */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PETS = "pets";
    public static final String PATH_PETS_ID = "pets/#";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PetContract() {}


    public static final class PetEntry implements BaseColumns {
        /**
         * table name
         * */
        public static final String TABLE_NAME = "pets";

        /**
         * table column
         * */
        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_WEIGHT = "weight";
        public static final String COLUMN_PET_GENDER = "gender";
        /**
         * Gender Contract
         * */
        public static final int GENDER_UNKNOWN = 0;

        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;


        public static boolean isValidGender(Integer gender) {
            boolean result = false;
            if (gender == GENDER_MALE || gender == GENDER_FEMALE || gender == GENDER_UNKNOWN ){
                result = true;
            }
            return result;
        }
    }
}
