package com.example.android.pets.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.android.pets.CatalogActivity;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetsDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by indraaguslesmana on 12/30/16.
 */

public class Helpers {
    private static final String TAG = Helpers.class.getSimpleName();

    public Helpers() {}

    private static long rowResult; // sometime
    /**
     * Insert Helpers to insert new data to database
     * */
    public static void DBinsertData(Context context, String name, String breed, int gender, int weight){

        PetsDBHelper mDbHelper = new PetsDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        /**
         * @param values this instance ContentValues. is "Key - Value pair" for database;
         * */
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, name);
        values.put(PetEntry.COLUMN_PET_BREED, breed);
        values.put(PetEntry.COLUMN_PET_GENDER, gender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, weight);

        rowResult = db.insert(PetEntry.TABLE_NAME, null, values);

        if (rowResult != -1){
            Constant.TOTAL_ROW = rowResult;
            Toast.makeText(context, "Row total: " + Constant.TOTAL_ROW, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "insert failure, return value: " + rowResult, Toast.LENGTH_SHORT).show();
        }
    }

    public static long DBdeleteData(Context context, String tableName) {
        PetsDBHelper mDbHelper = new PetsDBHelper(context);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("DELETE FROM " + tableName , null);
        return db.delete(tableName, null, null);
//        cursor.close();
    }

    public static void trayCursor (Context context) {
        PetsDBHelper mDbHelper = new PetsDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] projection = { PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_WEIGHT };
        String selection = PetEntry.COLUMN_PET_GENDER + "=?";
        String [] selectionArgs = new String[] { String.valueOf(PetEntry.GENDER_FEMALE)};

        Cursor c = db.query(PetEntry.TABLE_NAME, projection,
                selection, selectionArgs,
                null, null, null);

        List itemIds = new ArrayList<>();
        while(c.moveToNext()) {
            long itemId = c.getLong(
                    c.getColumnIndexOrThrow(PetEntry._ID));
            itemIds.add(itemId);
        }
        Log.i(TAG, itemIds.toString());
        c.close();

    }
}
