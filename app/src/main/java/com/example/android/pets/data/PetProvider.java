package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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

    @Override
    public boolean onCreate() {
        mDBhelper = new PetsDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
