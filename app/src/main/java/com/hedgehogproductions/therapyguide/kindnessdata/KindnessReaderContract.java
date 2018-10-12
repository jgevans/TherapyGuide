package com.hedgehogproductions.therapyguide.kindnessdata;

import android.provider.BaseColumns;

class KindnessReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private KindnessReaderContract() {}

    public static final String VERSION1_TABLE_NAME = "kindness";
    public static final String VERSION2_TABLE_NAME = "kindness_v2";

    // Old strings for use on upgrade
    public static final String COLUMN_NAME_KINDNESS_WORDS = "kindnessWords";
    public static final String COLUMN_NAME_KINDNESS_THOUGHTS = "kindnessThoughts";
    public static final String COLUMN_NAME_KINDNESS_ACTIONS = "kindnessActions";
    public static final String COLUMN_NAME_KINDNESS_SELF = "kindnessSelf";

    /* Inner class that defines the table contents */
    public static class KindnessDbEntry implements BaseColumns {
        public static final String TABLE_NAME = VERSION2_TABLE_NAME;
        public static final String COLUMN_NAME_DATE = "timestamp";
        public static final String COLUMN_NAME_KINDNESS_CATEGORY = "kindnessCategory";
        public static final String COLUMN_NAME_KINDNESS_VALUE = "kindnessValue";
        public static final String COLUMN_NAME_COMPLETE = "complete";
    }

}
