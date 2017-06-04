package com.hedgehogproductions.therapyguide.diarydata;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link InMemoryDiaryRepository}
 */
public class InMemoryDiaryRepositoryTest {

    private static final List<DiaryEntry> DIARY = Lists.newArrayList(new DiaryEntry(System.currentTimeMillis()-1000, "Entry One"),
            new DiaryEntry(System.currentTimeMillis(), "Entry Two"));

    private InMemoryDiaryRepository mDiaryRepository;

    @Mock
    private DiaryServiceApiImpl mServiceApi;

    @Mock
    private DiaryRepository.LoadDiaryEntryCallback mLoadDiaryEntryCallback;

    @Mock
    private DiaryRepository.LoadDiaryCallback mLoadDiaryCallback;

    @Captor
    private ArgumentCaptor<DiaryServiceApi.DiaryServiceCallback> mDiaryServiceCallbackCaptor;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

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
        // When two calls are issued to the diary repository
        twoLoadCallsToRepository(mLoadDiaryCallback);

        // Then entries where only requested once from Service API
        verify(mServiceApi).getAllDiaryEntries(any(DiaryServiceApi.DiaryServiceCallback.class));
    }


    @Test
    public void invalidateCache_doesNotCallTheServiceApi() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadDiaryCallback);

        // When data refresh is requested
        mDiaryRepository.refreshData();
        mDiaryRepository.getDiary(mLoadDiaryCallback); // Third call to API

        // The entries where requested twice from the Service API (Caching on first and third call)
        verify(mServiceApi, times(2)).getAllDiaryEntries(any(DiaryServiceApi.DiaryServiceCallback.class));
    }


    @Test
    public void getDiary_requestsAllDiaryEntriesFromServiceApi() {
        // When entries are requested from the entries repository
        mDiaryRepository.getDiary(mLoadDiaryCallback);

        // Then entries are loaded from the service API
        verify(mServiceApi).getAllDiaryEntries(any(DiaryServiceApi.DiaryServiceCallback.class));
    }

    @Test
    public void saveDiaryEntry_savesEntryToServiceAPIAndInvalidatesCache() {
        // Given a stub entry with timestamp and text
        DiaryEntry newEntry = new DiaryEntry(System.currentTimeMillis(), "Some Diary Text");
        // And a cached diary
        twoLoadCallsToRepository(mLoadDiaryCallback);
        assertThat(mDiaryRepository.mCachedEntries, is(not(nullValue())));

        // When a diary entry is saved to the diary repository
        mDiaryRepository.saveDiaryEntry(newEntry);

        // Then the service API was called
        verify(mServiceApi).saveDiaryEntry(newEntry);
        // And the  diary cache is cleared
        assertThat(mDiaryRepository.mCachedEntries, is(nullValue()));
    }

    @Test
    public void deleteDiaryEntry_deletesEntryThroughServiceAPIAndInvalidatesCache() {
        // Given a current entry
        DiaryEntry deletedEntry = DIARY.get(0);
        // And a cached diary
        twoLoadCallsToRepository(mLoadDiaryCallback);
        assertThat(mDiaryRepository.mCachedEntries, is(not(nullValue())));

        // When a diary entry is deleted from the diary repository
        mDiaryRepository.deleteDiaryEntry(deletedEntry);

        // Then the service API was called
        verify(mServiceApi).deleteDiaryEntry(deletedEntry);
        // And the  diary cache is cleared
        assertThat(mDiaryRepository.mCachedEntries, is(nullValue()));
    }

    @Test
    public void deleteNonExistentEntry_ThrowsException() {
        // Given a non existent entry
        DiaryEntry fakeEntry = new DiaryEntry(System.currentTimeMillis(), "This entry doesn't exist");
        // And a cached diary
        twoLoadCallsToRepository(mLoadDiaryCallback);
        assertThat(mDiaryRepository.mCachedEntries, is(not(nullValue())));

        // When a non-existent entry is updated, an exception is thrown
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("DiaryEntry not found");
        mDiaryRepository.deleteDiaryEntry(fakeEntry);
    }

    @Test
    public void updateDiaryEntry_updatesEntryThroughServiceAPIAndInvalidatesCache() {
        // Given a current entry
        DiaryEntry updatedEntry = DIARY.get(0);
        // And a cached diary
        twoLoadCallsToRepository(mLoadDiaryCallback);
        assertThat(mDiaryRepository.mCachedEntries, is(not(nullValue())));

        // When a diary entry is updated in the diary repository
        mDiaryRepository.updateDiaryEntry(updatedEntry);

        // Then the service API was called
        verify(mServiceApi).updateDiaryEntry(updatedEntry);
        // And the  diary cache is cleared
        assertThat(mDiaryRepository.mCachedEntries, is(nullValue()));
    }

    @Test
    public void updateNonExistentEntry_ThrowsException() {
        // Given a non existent entry
        DiaryEntry fakeEntry = new DiaryEntry(System.currentTimeMillis(), "This entry doesn't exist");
        // And a cached diary
        twoLoadCallsToRepository(mLoadDiaryCallback);
        assertThat(mDiaryRepository.mCachedEntries, is(not(nullValue())));

        // When a non-existent entry is updated, an exception is thrown
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("DiaryEntry not found");
        mDiaryRepository.updateDiaryEntry(fakeEntry);
    }

    @Test
    public void getDiaryEntry_requestsSingleEntryFromServiceApi() {
        final long timestamp = DIARY.get(0).getCreationTimestamp();

        // When an entry is requested from the diary repository
        mDiaryRepository.getDiaryEntry(timestamp, mLoadDiaryEntryCallback);

        // Then the entry is loaded from the service API
        verify(mServiceApi).getDiaryEntry(eq(timestamp), any(DiaryServiceApi.DiaryServiceCallback.class));
    }

    /**
     * Convenience method that issues two calls to the diary repository
     */
    private void twoLoadCallsToRepository(DiaryRepository.LoadDiaryCallback callback) {
        // When diary entries are requested from repository
        mDiaryRepository.getDiary(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mServiceApi).getAllDiaryEntries(mDiaryServiceCallbackCaptor.capture());

        // Trigger callback so diary is cached
        mDiaryServiceCallbackCaptor.getValue().onLoaded(DIARY);

        mDiaryRepository.getDiary(callback); // Second call to API
    }

}
