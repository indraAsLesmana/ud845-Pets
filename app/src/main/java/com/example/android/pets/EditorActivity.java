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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.helper.Constant;
import com.example.android.pets.helper.Helpers;

import java.util.zip.Inflater;


/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = EditorActivity.class.getSimpleName();

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;
    private Uri uriResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        if (getIntent().getData() != null){ // ingat! karna tadi setData, jadi getData
            uriResult = getIntent().getData();

            getLoaderManager().initLoader(Constant.EDITOR_ACTIVTY, null, this);
            setTitle(R.string.edit_pet); //change title to "Edit a pet"
        }else {
            setTitle(R.string.add_pet); //to "Add a pet"
        }

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        MenuItem actionDelete = menu.findItem(R.id.action_delete);
        /** hide, unhide delete action*/
        if (getIntent().getData() != null) {
            actionDelete.setVisible(false);
        } else {
            actionDelete.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                ContentValues contentValues = new ContentValues();
                if (getIntent().getData() != null){ //update data

                    if (inputValidation()){
                        contentValues.clear();
                        contentValues.put(PetEntry.COLUMN_PET_NAME,
                                mNameEditText.getText().toString());
                        contentValues.put(PetEntry.COLUMN_PET_BREED,
                                mBreedEditText.getText().toString());
                        contentValues.put(PetEntry.COLUMN_PET_GENDER,
                                mGenderSpinner.getSelectedItemPosition());
                        contentValues.put(PetEntry.COLUMN_PET_WEIGHT,
                                Integer.parseInt(mWeightEditText.getText().toString()));

                        String[] selectionArg = new String[]{uriResult.getLastPathSegment()};
                        int id = getContentResolver().update(uriResult, contentValues,
                                null, selectionArg);

                        if (id > 0){ // if result > 1, is succes effected row
                            finish();
                        }

                    }else {
                        Toast.makeText(this, "Please fill all form", Toast.LENGTH_LONG).show();
                    }

                } else { //save data

                    if (inputValidation()){
                        contentValues.clear();
                        contentValues.put(PetEntry.COLUMN_PET_NAME,
                                mNameEditText.getText().toString());
                        contentValues.put(PetEntry.COLUMN_PET_BREED,
                                mBreedEditText.getText().toString());
                        contentValues.put(PetEntry.COLUMN_PET_GENDER,
                                mGender);
                        contentValues.put(PetEntry.COLUMN_PET_WEIGHT,
                                Integer.parseInt(mWeightEditText.getText().toString()));

                        Uri uriResult = getContentResolver().insert(PetEntry.CONTENT_URI,
                                contentValues);

                        if (uriResult != null){
                            Log.i(TAG, uriResult.toString());
                            finish();
                        }

                    }else {
                        Toast.makeText(this, "Please fill all form", Toast.LENGTH_LONG).show();
                    }
                }

                return true;

            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     *check is form valid
     * */
    private boolean inputValidation () {
        boolean result = true;
        if (mNameEditText.getText().toString().trim().equals("") &&
                mWeightEditText.getText().toString().trim().equals("")){
            result = false;
        }
        return result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (uriResult == null) {
            return null;
        }
        return new CursorLoader(
                this,
                uriResult,
                Constant.PROJECTION_ALL_COLUMN,
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()) { //refactoring by udacity
            int nameColumn = data.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumn = data.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumn = data.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumn = data.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            String name = data.getString(nameColumn);
            String breed = data.getString(breedColumn);
            int gender = data.getInt(genderColumn);
            int weight = data.getInt(weightColumn);

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(String.valueOf(weight));
            mGenderSpinner.setSelection(gender);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText(null);
    }
}