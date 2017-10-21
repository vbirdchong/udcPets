package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by cccho on 2017/10/21.
 */

public final class PetContract {

    private PetContract() {}

    public static final class PetEntry implements BaseColumns {

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
    }
}
