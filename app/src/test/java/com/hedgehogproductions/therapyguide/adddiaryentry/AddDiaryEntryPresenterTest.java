package com.hedgehogproductions.therapyguide.adddiaryentry;


import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link AddDiaryEntryPresenter}
 */
public class AddDiaryEntryPresenterTest {

    @Mock
    private DiaryRepository mDiaryRepository;

    @Mock
    private AddDiaryEntryContract.View mAddDiaryEntryView;

    private AddDiaryEntryPresenter mAddDiaryEntryPresenter;

    @Before
    public void setupAddNotePresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAddDiaryEntryPresenter = new AddDiaryEntryPresenter(mDiaryRepository, mAddDiaryEntryView);
    }

    @Test
    public void saveEntryToRepository_savesData() {
        // When the presenter is asked to save a diaryEntry
        mAddDiaryEntryPresenter.saveNewDiaryEntry(System.currentTimeMillis(),"Test Entry One");

        // Then a DiaryEntry is saved,
        verify(mDiaryRepository).saveDiaryEntry(any(DiaryEntry.class));
        // ... and the diary is displayed
        verify(mAddDiaryEntryView).showDiaryView();
    }

    @Test
    public void saveEntry_emptyEntryShowsErrorUI() {
        // When the presenter is asked to save an empty note
        mAddDiaryEntryPresenter.saveNewDiaryEntry(0, "");

        // Then an empty entry error is shown in the UI
        verify(mAddDiaryEntryView).showEmptyEntryError();

    }

}
