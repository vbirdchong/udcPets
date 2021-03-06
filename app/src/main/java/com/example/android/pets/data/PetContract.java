package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by cccho on 2017/10/21.
 */

public final class PetContract {

    private PetContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.pets";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PETS = "pets";

    public static final class PetEntry implements BaseColumns {

        // The content URI to access the pet data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        // name of database table for pets
        public final static String TABLE_NAME = "pets";

        // ID number for pets
        // TYPE: INTEGER
        public final static String _ID = BaseColumns._ID;

        // name of the pet
        // TYPE: TEXT
        public final static String COLUMN_PET_NAME = "name";

        // breed of the pet
        // TYPE: TEXT
        public final static String COLUMN_PET_BREED = "breed";

        // gender of the pet
        // TYPE: INTEGER
        public final static String COLUMN_PET_GENDER = "gender";

        // weight of the pet
        // TYPE: INTEGER
        public final static String COLUMN_PET_WEIGHT = "weight";

        public static final int GENDER_UNKNOW = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static boolean isValidGender(Integer gender) {
            return gender == GENDER_MALE || gender == GENDER_FEMALE || gender == GENDER_UNKNOW;
        }
    }
}
