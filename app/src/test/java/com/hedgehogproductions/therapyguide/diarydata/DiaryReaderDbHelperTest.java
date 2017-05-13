package com.hedgehogproductions.therapyguide.diarydata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;


public class DiaryReaderDbHelperTest {

    private static final String ON_CREATE_SQL =
            "CREATE TABLE diary (_id INTEGER PRIMARY KEY,timestamp TEXT,diaryText TEXT)";

    private DiaryReaderDbHelper dbHelper;


    @Mock
    Context mMockContext;
    @Mock
    private SQLiteDatabase mMockDatabase;


    @Before
    public void setup() {
        dbHelper = new DiaryReaderDbHelper(mMockContext);
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void diaryReaderDbHelper_Creation_CorrectSQL() {
        dbHelper.onCreate(mMockDatabase);

        verify(mMockDatabase).execSQL(ON_CREATE_SQL);
    }

    @Test
    public void diaryReaderDbHelper_OnUpgrade_DoesNothing() {
        dbHelper.onUpgrade(mMockDatabase, 1, 2);

        verifyZeroInteractions(mMockDatabase);
    }

    @Test
    public void diaryReaderDbHelper_OnDowngrade_DoesNothing() {
        dbHelper.onDowngrade(mMockDatabase, 2, 1);

        verifyZeroInteractions(mMockDatabase);
    }

}
