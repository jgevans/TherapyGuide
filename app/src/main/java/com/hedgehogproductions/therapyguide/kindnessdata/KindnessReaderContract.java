package com.hedgehogproductions.therapyguide.kindnessdata;

import android.provider.BaseColumns;

class KindnessReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private KindnessReaderContract() {}

    /* Inner class that defines the table contents */
    public static class KindnessDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "kindness";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_KINDNESS_WORDS = "kindnessWords";
        public static final String COLUMN_NAME_KINDNESS_THOUGHTS = "kindnessThoughts";
        public static final String COLUMN_NAME_KINDNESS_ACTIONS = "kindnessActions";
        public static final String COLUMN_NAME_KINDNESS_SELF = "kindnessSelf";
        public static final String COLUMN_NAME_COMPLETE = "complete";
    }

}
