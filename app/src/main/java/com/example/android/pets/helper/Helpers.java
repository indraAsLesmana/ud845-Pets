package com.example.android.pets.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.pets.CatalogActivity;
import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetsDBHelper;

/**
 * Created by indraaguslesmana on 12/30/16.
 */

public class Helpers {
    public Helpers() {}

    /**
     * Insert Helpers to insert new data to database
     * */
    public static void insertData (Context context, String name, String breed, int gender, int weight){

        PetsDBHelper mDbHelper = new PetsDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        /**
         * @param values this instance ContentValues. is "Key - Value pair" for database;
         * */
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, name);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, breed);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, gender);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, weight);
        db.insert(PetContract.PetEntry.TABLE_NAME, null, values);
    }

    public static void deleteData(Context context, String tableName) {
        PetsDBHelper mDbHelper = new PetsDBHelper(context);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("DELETE FROM " + tableName , null);
        cursor.close();
    }
}
