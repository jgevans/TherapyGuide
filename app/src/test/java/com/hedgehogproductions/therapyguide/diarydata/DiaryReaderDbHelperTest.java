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
            "CREATE TABLE diary (_id INTEGER PRIMARY KEY,timestamp TEXT,diaryText TEXT,diaryText2 TEXT,diaryText3 TEXT,diaryText4 TEXT,diaryText5 TEXT)";
    private static final String ON_UPGRADE_SQL1 =
            "ALTER TABLE diary ADD diaryText2 TEXT;";
    private static final String ON_UPGRADE_SQL2 =
            "ALTER TABLE diary ADD diaryText3 TEXT;";
    private static final String ON_UPGRADE_SQL3 =
            "ALTER TABLE diary ADD diaryText4 TEXT;";
    private static final String ON_UPGRADE_SQL4 =
            "ALTER TABLE diary ADD diaryText5 TEXT;";
    private static final String ON_DOWNGRADE_SQL1 =
            "ALTER TABLE diary DROP COLUMN diaryText2;";
    private static final String ON_DOWNGRADE_SQL2 =
            "ALTER TABLE diary DROP COLUMN diaryText3;";
    private static final String ON_DOWNGRADE_SQL3 =
            "ALTER TABLE diary DROP COLUMN diaryText4;";
    private static final String ON_DOWNGRADE_SQL4 =
            "ALTER TABLE diary DROP COLUMN diaryText5;";

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
    public void diaryReaderDbHelper_OnUpgrade_1_2_CorrectSQL() {
        dbHelper.onUpgrade(mMockDatabase, 1, 2);

        verify(mMockDatabase).execSQL(ON_UPGRADE_SQL1);
        verify(mMockDatabase).execSQL(ON_UPGRADE_SQL2);
        verify(mMockDatabase).execSQL(ON_UPGRADE_SQL3);
        verify(mMockDatabase).execSQL(ON_UPGRADE_SQL4);
    }

    @Test
    public void diaryReaderDbHelper_OnDowngrade_2_1_CorrectSQL() {
        dbHelper.onDowngrade(mMockDatabase, 2, 1);

        verify(mMockDatabase).execSQL(ON_DOWNGRADE_SQL1);
        verify(mMockDatabase).execSQL(ON_DOWNGRADE_SQL2);
        verify(mMockDatabase).execSQL(ON_DOWNGRADE_SQL3);
        verify(mMockDatabase).execSQL(ON_DOWNGRADE_SQL4);
    }

}
