package com.hedgehogproductions.therapyguide.diarydata;

import android.provider.BaseColumns;



class DiaryReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DiaryReaderContract() {}

    /* Inner class that defines the table contents */
    public static class DiaryDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "diary";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_TEXT = "diaryText";
        public static final String COLUMN_NAME_TEXT2 = "diaryText2";
        public static final String COLUMN_NAME_TEXT3 = "diaryText3";
        public static final String COLUMN_NAME_TEXT4 = "diaryText4";
        public static final String COLUMN_NAME_TEXT5 = "diaryText5";
    }


}
