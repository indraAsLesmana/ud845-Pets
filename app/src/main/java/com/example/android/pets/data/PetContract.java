package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by indraaguslesmana on 12/30/16.
 */

public final class PetContract {

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
    }
}
