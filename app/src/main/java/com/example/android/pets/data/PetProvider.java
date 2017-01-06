package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.android.pets.R;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.helper.Constant;

import java.util.IllegalFormatConversionException;

/**
 * Created by indraaguslesmana on 1/3/17.
 */

public class PetProvider extends ContentProvider {
    /**
     * Tag for message LOG
     * */
    private static final String TAG = PetProvider.class.getSimpleName();
    
    /* get instance to DBhelper*/
    public PetsDBHelper mDBhelper;

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // TODO: Add 2 content URIs to URI matcher
        /** All PETs*/
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        /** PET by id*/
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);

        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

    }

    @Override
    public boolean onCreate() {
        mDBhelper = new PetsDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                //create Select * from pets;
                cursor = db.query(PetEntry.TABLE_NAME, projection, null, null, null, null, sortOrder, null);
                break;

            case PET_ID:
                //create select where from pets;
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        /** this make automaticly reload for every data change...*/
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPets (uri, values);
            default:
                throw new IllegalArgumentException("Error inserting queri" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                return deleteAlltable(uri);
            case PET_ID:
                return deleteById(uri, selection, selectionArgs);
            default:
                throw  new IllegalArgumentException("error delete data" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return updatePet(uri, values, selection, selectionArgs);
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDBhelper.getWritableDatabase();

        int rowUpdate = database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);

        /** notfied vied if change has been made*/
        if (rowUpdate != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return rowUpdate;
    }

    /** insert data method*/
    private Uri insertPets(Uri uri, ContentValues values) {
        if (!inputCheck(values)){
            Toast.makeText(getContext(), "reject by CoProvider", Toast.LENGTH_SHORT).show();
            return null;
        }

        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        long id = db.insert(
                PetEntry.TABLE_NAME,
                null,
                values);

        /** make toast for every new insert row, if success*/
        Uri uriResult = ContentUris.withAppendedId(uri, id);
        if (id != -1){
            Toast.makeText(getContext(), "Insert Success at: \n" +
                    uriResult.toString(), Toast.LENGTH_SHORT).show();

            //notify if change has been made
            getContext().getContentResolver().notifyChange(uri, null);

        }else {
            Toast.makeText(getContext(), "insert failure, return value: " +
                    id, Toast.LENGTH_SHORT).show();
        }

        return uriResult;
    }

    /** delete by id method */
    private int deleteById(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        selection = PetEntry._ID + "=?";
        int rowDeleted = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

        /** notify if delete success*/
        if(rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    /** delete all data in table*/
    private int deleteAlltable(Uri uri) {
        SQLiteDatabase db = mDBhelper.getWritableDatabase();

        int rowDeleted = db.delete(PetEntry.TABLE_NAME, null, null);

        /** notify if delete success*/
        if(rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    /**
     * sanity check for vall values
     * */
    private boolean inputCheck (ContentValues values){
        boolean inputResult = true;
        if (values.getAsString(PetEntry.COLUMN_PET_NAME).isEmpty() ||
                values.getAsString(PetEntry.COLUMN_PET_BREED).isEmpty() ||
                values.getAsInteger(PetEntry.COLUMN_PET_GENDER) == null ||
                values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT) == null) {

            return inputResult = false;
        }

        return inputResult;
    }
}
