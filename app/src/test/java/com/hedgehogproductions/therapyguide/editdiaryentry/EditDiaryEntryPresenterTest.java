package com.hedgehogproductions.therapyguide.editdiaryentry;


import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for the implementation of {@link EditDiaryEntryPresenter}
 */
public class EditDiaryEntryPresenterTest {

    private static final long TIMESTAMP = System.currentTimeMillis();
    private static final long INVALID_TIMESTAMP = 1;
    private static final String TEXT = "Test Text";

    @Mock
    private DiaryRepository mDiaryRepository;

    @Mock
    private EditDiaryEntryContract.View mEditDiaryEntryView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<DiaryRepository.LoadDiaryEntryCallback> mLoadDiaryEntryCallbackCaptor;

    private EditDiaryEntryPresenter mEditDiaryEntryPresenter;

    @Before
    public void setupAddNotePresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mEditDiaryEntryPresenter = new EditDiaryEntryPresenter(mDiaryRepository, mEditDiaryEntryView);
    }

    @Test
    public void openDiaryEntry_ShowsEntryDetails() {
        // Given a diary entry
        DiaryEntry entry = new DiaryEntry(TIMESTAMP, TEXT);

        // When the presenter is asked to open the entry
        mEditDiaryEntryPresenter.openDiaryEntry(TIMESTAMP);

        // Then the entry is loaded, the callback is captured
        verify(mDiaryRepository).getDiaryEntry(eq(TIMESTAMP), mLoadDiaryEntryCallbackCaptor.capture());

        // When the entry is finally loaded
        mLoadDiaryEntryCallbackCaptor.getValue().onEntryLoaded(entry); // Trigger callback

        // Then the text is shown in UI
        verify(mEditDiaryEntryView).showDiaryText(TEXT);
    }

    @Test
    public void openNonExistentDiaryEntry_DisplaysError() {
        // When loading of an entry is requested with an invalid timestamp.
        mEditDiaryEntryPresenter.openDiaryEntry(INVALID_TIMESTAMP);

        // Then the entry with invalid timestamp is attempted to be loaded from model
        // and the callback is captured.
        verify(mDiaryRepository).getDiaryEntry(eq(INVALID_TIMESTAMP), mLoadDiaryEntryCallbackCaptor.capture());

        // When entry is finally (not) loaded
        mLoadDiaryEntryCallbackCaptor.getValue().onEntryLoaded(null); // Trigger callback

        // Then the missing entry UI is shown
        verify(mEditDiaryEntryView).showMissingEntryError();
    }

    @Test
    public void saveNewEntryToRepository_SavesData() {
        // When the presenter is asked to save a diaryEntry
        mEditDiaryEntryPresenter.saveNewDiaryEntry(System.currentTimeMillis(),"Test Entry One");

        // Then a DiaryEntry is saved,
        verify(mDiaryRepository).saveDiaryEntry(any(DiaryEntry.class));
        // ... and the diary is displayed
        verify(mEditDiaryEntryView).showDiaryView();
    }

    @Test
    public void saveNewEntry_emptyEntryShowsErrorUI() {
        // When the presenter is asked to save an empty note
        mEditDiaryEntryPresenter.saveNewDiaryEntry(0, "");

        // Then an empty entry error is shown in the UI
        verify(mEditDiaryEntryView).showEmptyEntryError();

    }

    @Test
    public void saveUpdatedEntryToRepository_SavesData() {
        // Given a valid timestamp
        mEditDiaryEntryPresenter.setCreationTimestamp(TIMESTAMP);

        // When the presenter is asked to open the entry
        mEditDiaryEntryPresenter.updateDiaryEntry(TEXT);

        // Then a DiaryEntry is updated,
        verify(mDiaryRepository).updateDiaryEntry(any(DiaryEntry.class));
        // ... and the diary is displayed
        verify(mEditDiaryEntryView).showDiaryView();
    }

    @Test
    public void saveUpdatedEntry_emptyEntryShowsErrorUI() {
        // When the presenter is asked to save an empty note
        mEditDiaryEntryPresenter.updateDiaryEntry("");

        // Then an empty entry error is shown in the UI
        verify(mEditDiaryEntryView).showEmptyEntryError();

    }
}
