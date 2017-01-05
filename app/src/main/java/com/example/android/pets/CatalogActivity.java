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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

import com.example.android.pets.adapter.PetCursorAdapter;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.helper.Constant;
import com.example.android.pets.helper.Helpers;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = CatalogActivity.class.getSimpleName();
    private ContentValues contentValues = new ContentValues();

    /** Projection */
    private final String [] PROJECTION_ALL_COLUMN = {
            PetEntry._ID,
            PetEntry.COLUMN_PET_NAME,
            PetEntry.COLUMN_PET_BREED,
            PetEntry.COLUMN_PET_GENDER,
            PetEntry.COLUMN_PET_WEIGHT
    };

    private final String[] PROJECTION_ALL_3_COLUMN = {
            PetEntry._ID,
            PetEntry.COLUMN_PET_NAME,
            PetEntry.COLUMN_PET_BREED,
    };


    private ListView main_listview;
    private PetCursorAdapter mCursorAdapter;
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
        RelativeLayout empetyView = (RelativeLayout) findViewById(R.id.empety_list);

        //view initialized
        main_listview = (ListView) findViewById(R.id.main_listview);
        main_listview.setEmptyView(empetyView);


        main_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor itemCursor = (Cursor) mCursorAdapter.getItem(position);

                String nameResult = itemCursor.getString(itemCursor.
                        getColumnIndexOrThrow(PetEntry.COLUMN_PET_NAME));
                String breedResult = itemCursor.getString(itemCursor.
                        getColumnIndexOrThrow(PetEntry.COLUMN_PET_BREED));

                Toast.makeText(CatalogActivity.this, nameResult, Toast.LENGTH_SHORT).show();

            }
        });
        //inisitalized adapter
        mCursorAdapter = new PetCursorAdapter(this, null);
        main_listview.setAdapter(mCursorAdapter);

        /**displayDatabaseInfo(); chage with this. CursorLoader.
         *
         * do in Background thread like AsynctaskLoader*/
        getLoaderManager().initLoader(Constant.CATALOG_ACTIVITY, null, this);
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
                contentValues.clear();
                contentValues.put(PetEntry.COLUMN_PET_NAME, "dummyName");
                contentValues.put(PetEntry.COLUMN_PET_BREED, "breedName");
                contentValues.put(PetEntry.COLUMN_PET_GENDER, 1);
                contentValues.put(PetEntry.COLUMN_PET_WEIGHT, 6);
                getContentResolver().insert(PetEntry.CONTENT_URI,contentValues);

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

                return true;

            case R.id.action_delete_all_entries:
                // Do nothing for now
                getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                PROJECTION_ALL_3_COLUMN,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
