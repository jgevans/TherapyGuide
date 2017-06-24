package com.hedgehogproductions.therapyguide.diary;

import com.google.common.collect.Lists;
import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link DiaryPresenter}
 */
public class DiaryPresenterTest {

    private static final List<DiaryEntry> DIARY = Lists.newArrayList(
            new DiaryEntry(System.currentTimeMillis(), "Entry1a", "b", "c", "d", "e"),
            new DiaryEntry(System.currentTimeMillis(), "Entry2a", "b", "c", "d", "e"));


    @Mock
    private DiaryRepository mDiaryRepository;

    @Mock
    private DiaryContract.View mDiaryView;


    private DiaryPresenter mDiaryPresenter;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<DiaryRepository.LoadDiaryCallback> mLoadDiaryCallbackCaptor;


    @Before
    public void setupDiaryPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDiaryPresenter = new DiaryPresenter(mDiaryRepository, mDiaryView);
    }

    @Test
    public void loadDiaryFromRepositoryAndLoadIntoView() {
        // Given an initialized DiaryPresenter with initialised diary entries
        // When loading of Diary is requested
        mDiaryPresenter.loadDiary();

        // The callback is captured and invoked with stubbed notes
        verify(mDiaryRepository).getDiary(mLoadDiaryCallbackCaptor.capture());
        mLoadDiaryCallbackCaptor.getValue().onDiaryLoaded(DIARY);

        // Then the diary entries are shown in the UI
        verify(mDiaryView).showDiary(DIARY);
    }

    @Test
    public void clickOnFab_ShowsAddDiaryEntryUi() {
        // When adding a new diary entry
        mDiaryPresenter.addNewDiaryEntry();

        // Then add diary entry UI is shown
        verify(mDiaryView).showAddDiaryEntry();
    }

    @Test
    public void instigateDeleteEntryRequest_ShowsDeletionMessage() {
        final int position = 3;

        // When trying to start deleting an entry
        mDiaryPresenter.instigateDiaryEntryDeletion(position);

        // Then deletion confirmation is shown
        verify(mDiaryView).showDiaryEntryDeletionMessage(position);
    }

    @Test
    public void deleteEntry_RemovesFromRepo() {
        // When asked to delete an entry
        mDiaryPresenter.deleteDiaryEntry(DIARY.get(0));

        // The deletion is sent to the repository
        verify(mDiaryRepository).deleteDiaryEntry(DIARY.get(0));
    }
}