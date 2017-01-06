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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = CatalogActivity.class.getSimpleName();
    private ContentValues contentValues = new ContentValues();

    private Cursor checkTable;

    private ListView main_listview;
    private PetCursorAdapter mCursorAdapter;

    /** cheking is table empety*/
    private boolean isTableEmpety;

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
                /**
                 * if you want build path use Uri.withAppendedpath
                 * */
                Uri uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.setData(uri); // refactoring best with Uri with intent.setData

                startActivity(intent);
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
            case R.id.action_delete_all_entries:
                // Do nothing for now
                if (!isTableEmpety){
                    showDeleteConfirmationDialog();
                }else {
                    Toast.makeText(this, "data is empety", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                Constant.PROJECTION_3_COLUMN,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        /** checking is table empety or not*/
        if(data.moveToFirst()){  // if on table can move to first data, table not empety
            isTableEmpety = false;
        }else {
            isTableEmpety = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        int rowEffected = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        if (rowEffected > 0){
            Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        }
    }
}
