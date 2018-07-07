package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class KindnessReaderDbHelperTest {

    private static final String ON_CREATE_SQL =
            "CREATE TABLE kindness (_id INTEGER PRIMARY KEY,timestamp TEXT,kindnessWords TEXT,kindnessThoughts TEXT,kindnessActions TEXT,kindnessSelf TEXT,complete INTEGER)";

    private KindnessReaderDbHelper dbHelper;

    @Mock
    private Context mMockContext;
    @Mock
    private SQLiteDatabase mMockDatabase;


    @Before
    public void setup() {
        dbHelper = new KindnessReaderDbHelper(mMockContext);
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void kindnessReaderDbHelper_Creation_CorrectSQL() {
        dbHelper.onCreate(mMockDatabase);

        verify(mMockDatabase).execSQL(ON_CREATE_SQL);
    }

}
