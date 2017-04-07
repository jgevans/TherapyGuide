package com.hedgehogproductions.therapyguide.diarydata;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link InMemoryDiaryRepository}
 */
public class InMemoryDiaryRepositoryTest {

    private static List<DiaryEntry> DIARY = Lists.newArrayList(new DiaryEntry(System.currentTimeMillis()-1000, "Entry One"),
            new DiaryEntry(System.currentTimeMillis(), "Entry Two"));

    private InMemoryDiaryRepository mDiaryRepository;

    @Mock
    private DiaryServiceApiImpl mServiceApi;

    @Mock
    private DiaryRepository.LoadDiaryCallback mLoadDiaryCallback;

    @Captor
    private ArgumentCaptor<DiaryServiceApi.DiaryServiceCallback> mDiaryServiceCallbackCaptor;



    @Before
    public void setupDiaryRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDiaryRepository = new InMemoryDiaryRepository(mServiceApi);
    }


    @Test
    public void getDiaryEntries_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the notes repository
        twoLoadCallsToRepository(mLoadDiaryCallback);

        // Then notes where only requested once from Service API
        verify(mServiceApi).getAllDiaryEntries(any(DiaryServiceApi.DiaryServiceCallback.class));
    }


    @Test
    public void invalidateCache_doesNotCallTheServiceApi() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadDiaryCallback);

        // When data refresh is requested
        mDiaryRepository.refreshData();
        mDiaryRepository.getDiary(mLoadDiaryCallback); // Third call to API

        // The notes where requested twice from the Service API (Caching on first and third call)
        verify(mServiceApi, times(2)).getAllDiaryEntries(any(DiaryServiceApi.DiaryServiceCallback.class));
    }


    @Test
    public void getNotes_requestsAllNotesFromServiceApi() {
        // When notes are requested from the notes repository
        mDiaryRepository.getDiary(mLoadDiaryCallback);

        // Then notes are loaded from the service API
        verify(mServiceApi).getAllDiaryEntries(any(DiaryServiceApi.DiaryServiceCallback.class));
    }



    /**
     * Convenience method that issues two calls to the notes repository
     */
    private void twoLoadCallsToRepository(DiaryRepository.LoadDiaryCallback callback) {
        // When notes are requested from repository
        mDiaryRepository.getDiary(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mServiceApi).getAllDiaryEntries(mDiaryServiceCallbackCaptor.capture());

        // Trigger callback so notes are cached
        mDiaryServiceCallbackCaptor.getValue().onLoaded(DIARY);

        mDiaryRepository.getDiary(callback); // Second call to API
    }

}
