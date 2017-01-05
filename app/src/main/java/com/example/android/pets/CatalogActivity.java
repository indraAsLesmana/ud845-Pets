/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.adapter.PetCursorAdapter;
import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetsDBHelper;
import com.example.android.pets.helper.Constant;
import com.example.android.pets.helper.Helpers;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private static final String TAG = CatalogActivity.class.getSimpleName();
    private ContentValues contentValues = new ContentValues();

    private ListView main_listview;
    private PetCursorAdapter adapter;
    private Cursor cursorResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        /** Projection */
        String [] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        main_listview = (ListView) findViewById(R.id.main_listview);

        cursorResult = getContentResolver().query(PetEntry.CONTENT_URI, projection, null, null, null);

        adapter = new PetCursorAdapter(this, cursorResult);
        main_listview.setAdapter(adapter);

//        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
//    private void displayDatabaseInfo() {
//        // To access our database, we instantiate our subclass of SQLiteOpenHelper
//        // and pass the context, which is the current activity.
//        PetsDBHelper mDbHelper = new PetsDBHelper(this);
//
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Perform this raw SQL query "SELECT * FROM pets"
//        // to get a Cursor that contains all rows from the pets table.
//        /**
//         *if you want select all like this, is can shortcut with Raw sql stament
//         *like " * " for SELECT * FROM ...
//         */
//        String [] projection = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT
//                };
//
//        /*Cursor cursor = db.query(
//                PetEntry.TABLE_NAME,
//                projection,
//                null, null, null, null, null);
//        */
//        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI, projection, null, null, null);
//
//        //define column index position you want to view
//        int columnId = cursor.getColumnIndex(PetEntry._ID);
//        int columnName = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
//        int columnBreed = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
//        int columnGender = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
//        int columnWeight = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
//
//        TextView displayView = (TextView) findViewById(R.id.text_view_pet);
//        displayView.setText("Number of rows in pets database table: " + cursor.getCount() + "\n");
//
//        displayView.append(PetEntry._ID + " - " + PetEntry.COLUMN_PET_NAME + "\n");
//        try {
//            // Display the number of rows in the Cursor (which reflects the number of rows in the
//            // pets table in the database).
//
//            while (cursor.moveToNext()) {
//                int rowId = cursor.getInt(columnId);
//                String nameValue = cursor.getString(columnName);
//                String breedValue = cursor.getString(columnBreed);
//                int genderValue = cursor.getInt(columnGender);
//                int weightValue = cursor.getInt(columnWeight);
//
//                String genderResult;
//                switch (genderValue){
//                    case PetEntry.GENDER_UNKNOWN:
//                        genderResult = "Unknown";
//                        break;
//                    case PetEntry.GENDER_FEMALE:
//                        genderResult = "Female";
//                        break;
//                    case PetEntry.GENDER_MALE:
//                        genderResult = "Male";
//                        break;
//                    default:
//                        genderResult = "Unkown";
//                }
//
//                displayView.append("\n" +
//                        rowId + " - " +
//                        nameValue + " - " +
//                        breedValue + " - " +
//                        genderResult + " - " +
//                        weightValue
//                );
//            }
//
//        }catch (SQLiteException e){
//            Log.e(TAG, e.getMessage());
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//    }

    /**
     * on udacity with onStart, but i think with onResume great
     * */
    @Override
    protected void onResume() {
        super.onResume();
//        displayDatabaseInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    /**
     * Since getWritableDatabase() and getReadableDatabase() are expensive to call when the database is closed,
     * you should leave your database connection open for as long as you possibly need to access it. Typically,
     * it is optimal to close the database in the onDestroy() of the calling Activity.
     * */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
//                Helpers.DBinsertData(this, "Toto", "Tarrier", PetEntry.GENDER_MALE, 7);
                contentValues.clear();
                contentValues.put(PetEntry.COLUMN_PET_NAME, "dummyName");
                contentValues.put(PetEntry.COLUMN_PET_BREED, "breedName");
                contentValues.put(PetEntry.COLUMN_PET_GENDER, 1);
                contentValues.put(PetEntry.COLUMN_PET_WEIGHT, 6);
                getContentResolver().insert(PetEntry.CONTENT_URI,contentValues);

//                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_update_dummy_data:
                contentValues.clear();
                contentValues.put(PetEntry.COLUMN_PET_NAME, "omen ok");
                contentValues.put(PetEntry.COLUMN_PET_BREED, "breedName Update");
                contentValues.put(PetEntry.COLUMN_PET_GENDER, 1);
                contentValues.put(PetEntry.COLUMN_PET_WEIGHT, 6);

                String selection = PetEntry._ID + "=?";
                String [] selectionArgs = new String []{"12", "14"};

                getContentResolver().update(PetEntry.CONTENT_URI, contentValues, null, null);

//                displayDatabaseInfo();
                return true;

            case R.id.action_delete_all_entries:
                // Do nothing for now
//                Constant.TOTAL_ROW = Helpers.DBdeleteData(this, PetEntry.TABLE_NAME);
                getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
//                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
