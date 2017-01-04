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

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
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
                return deleteAlltable();
            case PET_ID:
                return deleteById(selection, selectionArgs);
            default:
                throw  new IllegalArgumentException("error delete data" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
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
            Toast.makeText(getContext(), "Insert Success at: \n" + uriResult.toString(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "insert failure, return value: " + id, Toast.LENGTH_SHORT).show();
        }

        return uriResult;
    }

    /** delete by id method */
    private int deleteById(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        return db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
    }

    /** delete all data in table*/
    private int deleteAlltable() {
        SQLiteDatabase db = mDBhelper.getWritableDatabase();
        return db.delete(PetEntry.TABLE_NAME, null, null);
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
