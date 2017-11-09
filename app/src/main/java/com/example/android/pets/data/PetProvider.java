package com.example.android.pets.data;

/**
 * Created by cccho on 2017/10/25.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.android.pets.data.PetContract.CONTENT_AUTHORITY;
import static com.example.android.pets.data.PetContract.PATH_PETS;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    public PetDbHelper mPetDbHelper;

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
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PETS, PETS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PETS + "/#", PET_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mPetDbHelper = new PetDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mPetDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection,
                                        selection, selectionArgs, null, null, sortOrder);
                break;

            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection,
                                        selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can not query unknown URI " + uri);
        }

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion in not supported for " + uri);
        }
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not support for " + uri);
        }
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return deletePet(selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return deletePet(selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delete not support for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown " + uri + " with match " + match);
        }
    }

    private Uri insertPet(Uri uri, ContentValues contentValues) {

        String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        String breed = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_BREED);
        if (breed == null) {
            throw new IllegalArgumentException("Pet requires a breed");
        }

        int gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        if (gender != PetContract.PetEntry.GENDER_UNKNOW ||
                gender != PetContract.PetEntry.GENDER_FEMALE ||
                gender != PetContract.PetEntry.GENDER_MALE) {
            throw new IllegalArgumentException("Pet gender is not accepted");
        }

        int weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if (weight < 0) {
            throw new IllegalArgumentException("Pet weight is not correct " + weight);
        }

        // add data into the database
        SQLiteDatabase database = mPetDbHelper.getWritableDatabase();
        long id = database.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    private int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)) {
            String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet require a name");
            }
        }

        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if (gender == null || PetContract.PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet require valid gender");
            }
        }

        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if (weight == null || weight < 0) {
                throw new IllegalArgumentException("Pet require valid weight");
            }
        }

        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mPetDbHelper.getWritableDatabase();
        return database.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    private int deletePet(String selection, String[] selectionArgs) {
        SQLiteDatabase database = mPetDbHelper.getWritableDatabase();

        return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
    }
}