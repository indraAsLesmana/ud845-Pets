package com.example.android.pets.helper;

import com.example.android.pets.data.PetContract;

/**
 * Created by indraaguslesmana on 1/1/17.
 */

public class Constant {
    /** to editify unieqely per activty*/
    public static final int CATALOG_ACTIVITY = 01;
    public static final int EDITOR_ACTIVTY = 02;

    public static long TOTAL_ROW;

    /** Projection */
    public static final String [] PROJECTION_ALL_COLUMN = {
            PetContract.PetEntry._ID,
            PetContract.PetEntry.COLUMN_PET_NAME,
            PetContract.PetEntry.COLUMN_PET_BREED,
            PetContract.PetEntry.COLUMN_PET_GENDER,
            PetContract.PetEntry.COLUMN_PET_WEIGHT
    };
    public static final String[] PROJECTION_WITHOUT_ID = {
            PetContract.PetEntry.COLUMN_PET_NAME,
            PetContract.PetEntry.COLUMN_PET_BREED,
            PetContract.PetEntry.COLUMN_PET_GENDER,
            PetContract.PetEntry.COLUMN_PET_WEIGHT
    };

    public static final String[] PROJECTION_3_COLUMN = {
            PetContract.PetEntry._ID,
            PetContract.PetEntry.COLUMN_PET_NAME,
            PetContract.PetEntry.COLUMN_PET_BREED,
    };

}
