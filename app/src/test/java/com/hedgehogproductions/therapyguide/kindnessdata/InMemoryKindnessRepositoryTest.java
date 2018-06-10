package com.hedgehogproductions.therapyguide.kindnessdata;


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
 * Unit tests for the implementation of {@link InMemoryKindnessRepository}
 */
public class InMemoryKindnessRepositoryTest {

    private static final List<KindnessEntry> KindnessDiary = Lists.newArrayList(
            new KindnessEntry(System.currentTimeMillis()-1000, KindnessWords.CALL, KindnessThoughts.GOSSIP, KindnessActions.DOOR, KindnessSelf.FRIEND),
            new KindnessEntry(System.currentTimeMillis(), KindnessWords.SKILLS, KindnessThoughts.LISTEN, KindnessActions.CAKE, KindnessSelf.COOK));

    private InMemoryKindnessRepository mKindnessRepository;

    @Mock
    private KindnessServiceApiImpl mServiceApi;

    @Mock
    private KindnessRepository.LoadKindnessEntryCallback mLoadKindnessEntryCallback;

    @Mock
    private KindnessRepository.LoadKindnessCallback mLoadKindnessCallback;

    @Captor
    private ArgumentCaptor<KindnessServiceApi.KindnessServiceCallback> mKindnessServiceCallbackCaptor;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setupKindnessRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mKindnessRepository = new InMemoryKindnessRepository(mServiceApi);
    }


    @Test
    public void getKindnessEntries_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the Kindness repository
        twoLoadCallsToRepository(mLoadKindnessCallback);

        // Then entries where only requested once from Service API
        verify(mServiceApi).getAllKindnessEntries(any(KindnessServiceApi.KindnessServiceCallback.class));
    }


    @Test
    public void invalidateCache_doesNotCallTheServiceApi() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadKindnessCallback);

        // When data refresh is requested
        mKindnessRepository.refreshData();
        mKindnessRepository.getKindnessDiary(mLoadKindnessCallback); // Third call to API

        // The entries where requested twice from the Service API (Caching on first and third call)
        verify(mServiceApi, times(2)).getAllKindnessEntries(any(KindnessServiceApi.KindnessServiceCallback.class));
    }


    @Test
    public void getKindness_requestsAllKindnessEntriesFromServiceApi() {
        // When entries are requested from the entries repository
        mKindnessRepository.getKindnessDiary(mLoadKindnessCallback);

        // Then entries are loaded from the service API
        verify(mServiceApi).getAllKindnessEntries(any(KindnessServiceApi.KindnessServiceCallback.class));
    }

    @Test
    public void saveKindnessEntry_savesEntryToServiceAPIAndInvalidatesCache() {
        // Given a stub entry with timestamp and text
        KindnessEntry newEntry = new KindnessEntry(
                System.currentTimeMillis(), KindnessWords.ABILITIES, KindnessThoughts.DOUBT, KindnessActions.BUY, KindnessSelf.MOVIE);
        // And a cached Kindness
        twoLoadCallsToRepository(mLoadKindnessCallback);
        assertThat(mKindnessRepository.mCachedEntries, is(not(nullValue())));

        // When a Kindness entry is saved to the Kindness repository
        mKindnessRepository.saveKindnessEntry(newEntry);

        // Then the service API was called
        verify(mServiceApi).saveKindnessEntry(newEntry);
        // And the  Kindness cache is cleared
        assertThat(mKindnessRepository.mCachedEntries, is(nullValue()));
    }

    @Test
    public void deleteKindnessEntry_deletesEntryThroughServiceAPIAndInvalidatesCache() {
        // Given a current entry
        KindnessEntry deletedEntry = KindnessDiary.get(0);
        // And a cached Kindness
        twoLoadCallsToRepository(mLoadKindnessCallback);
        assertThat(mKindnessRepository.mCachedEntries, is(not(nullValue())));

        // When a Kindness entry is deleted from the Kindness repository
        mKindnessRepository.deleteKindnessEntry(deletedEntry);

        // Then the service API was called
        verify(mServiceApi).deleteKindnessEntry(deletedEntry);
        // And the  Kindness cache is cleared
        assertThat(mKindnessRepository.mCachedEntries, is(nullValue()));
    }

    @Test
    public void deleteNonExistentEntry_ThrowsException() {
        // Given a non existent entry
        KindnessEntry fakeEntry = new KindnessEntry(
                System.currentTimeMillis(), KindnessWords.APPEARANCE, KindnessThoughts.SUCCESS, KindnessActions.TRAFFIC, KindnessSelf.VOLUNTEER);
        // And a cached Kindness
        twoLoadCallsToRepository(mLoadKindnessCallback);
        assertThat(mKindnessRepository.mCachedEntries, is(not(nullValue())));

        // When a non-existent entry is updated, an exception is thrown
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("KindnessEntry not found");
        mKindnessRepository.deleteKindnessEntry(fakeEntry);
    }

    @Test
    public void updateKindnessEntry_updatesEntryThroughServiceAPIAndInvalidatesCache() {
        // Given a current entry
        KindnessEntry updatedEntry = KindnessDiary.get(0);
        // And a cached Kindness
        twoLoadCallsToRepository(mLoadKindnessCallback);
        assertThat(mKindnessRepository.mCachedEntries, is(not(nullValue())));

        // When a Kindness entry is updated in the Kindness repository
        mKindnessRepository.updateKindnessEntry(updatedEntry);

        // Then the service API was called
        verify(mServiceApi).updateKindnessEntry(updatedEntry);
        // And the Kindness cache is cleared
        assertThat(mKindnessRepository.mCachedEntries, is(nullValue()));
    }

    @Test
    public void updateNonExistentEntry_ThrowsException() {
        // Given a non existent entry
        KindnessEntry fakeEntry = new KindnessEntry(
                System.currentTimeMillis(), KindnessWords.LOVE, KindnessThoughts.WISHES, KindnessActions.HELP, KindnessSelf.DRAW);
        // And a cached Kindness
        twoLoadCallsToRepository(mLoadKindnessCallback);
        assertThat(mKindnessRepository.mCachedEntries, is(not(nullValue())));

        // When a non-existent entry is updated, an exception is thrown
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("Entry not found");
        mKindnessRepository.updateKindnessEntry(fakeEntry);
    }

    @Test
    public void getKindnessEntry_requestsSingleEntryFromServiceApi() {
        final long timestamp = KindnessDiary.get(0).getCreationTimestamp();

        // When an entry is requested from the Kindness repository
        mKindnessRepository.getKindnessEntry(timestamp, mLoadKindnessEntryCallback);

        // Then the entry is loaded from the service API
        verify(mServiceApi).getKindnessEntry(eq(timestamp), any(KindnessServiceApi.KindnessServiceCallback.class));
    }

    /**
     * Convenience method that issues two calls to the Kindness repository
     */
    private void twoLoadCallsToRepository(KindnessRepository.LoadKindnessCallback callback) {
        // When Kindness entries are requested from repository
        mKindnessRepository.getKindnessDiary(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mServiceApi).getAllKindnessEntries(mKindnessServiceCallbackCaptor.capture());

        // Trigger callback so Kindness is cached
        mKindnessServiceCallbackCaptor.getValue().onLoaded(KindnessDiary);

        mKindnessRepository.getKindnessDiary(callback); // Second call to API
    }

}
