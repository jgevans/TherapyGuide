package com.hedgehogproductions.therapyguide.editkindnessentry;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepository;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link EditKindnessEntryPresenter}
 */

public class EditKindnessEntryPresenterTest {

    private static final long TIMESTAMP = System.currentTimeMillis();
    private static final long INVALID_TIMESTAMP = 1;

    private static final KindnessWords WORDS = KindnessWords.APPEARANCE;
    private static final KindnessThoughts THOUGHTS = KindnessThoughts.DOUBT;
    private static final KindnessActions ACTIONS = KindnessActions.BUY;
    private static final KindnessSelf SELF = KindnessSelf.KIND;

    @Mock
    private KindnessRepository mKindnessRepository;

    @Mock
    private EditKindnessEntryContract.View mEditKindnessEntryView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<KindnessRepository.LoadKindnessEntryCallback> mLoadKindnessEntryCallbackCaptor;

    private EditKindnessEntryPresenter mEditKindnessEntryPresenter;

    @Before
    public void setupEditKindnessEntryPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mEditKindnessEntryPresenter = new EditKindnessEntryPresenter(mKindnessRepository, mEditKindnessEntryView);
    }

    @Test
    public void openKindnessEntry_ShowsEntryDetails() {
        // Given a kindness entry
        KindnessEntry entry = new KindnessEntry(TIMESTAMP, WORDS, THOUGHTS, ACTIONS, SELF, false);

        // When the presenter is asked to open the entry
        mEditKindnessEntryPresenter.openKindnessEntry(TIMESTAMP);

        // Then the entry is loaded, the callback is captured
        verify(mKindnessRepository).getKindnessEntry(eq(TIMESTAMP), mLoadKindnessEntryCallbackCaptor.capture());

        // When the entry is finally loaded
        mLoadKindnessEntryCallbackCaptor.getValue().onEntryLoaded(entry); // Trigger callback

        // Then the text is shown in UI
        verify(mEditKindnessEntryView).showKindnessDetail(WORDS, THOUGHTS, ACTIONS, SELF, false);
    }

    @Test
    public void openNonExistentKindnessEntry_DisplaysError() {
        // When loading of an entry is requested with an invalid timestamp.
        mEditKindnessEntryPresenter.openKindnessEntry(INVALID_TIMESTAMP);

        // Then the entry with invalid timestamp is attempted to be loaded from model
        // and the callback is captured.
        verify(mKindnessRepository).getKindnessEntry(eq(INVALID_TIMESTAMP), mLoadKindnessEntryCallbackCaptor.capture());

        // When entry is finally (not) loaded
        mLoadKindnessEntryCallbackCaptor.getValue().onEntryLoaded(null); // Trigger callback

        // Then the missing entry UI is shown
        verify(mEditKindnessEntryView).showMissingEntryError();
    }

    @Test
    public void saveNewEntryToRepository_SavesData() {
        // When the presenter is asked to save a kindnessEntry
        mEditKindnessEntryPresenter.saveNewKindnessEntry(TIMESTAMP, WORDS, THOUGHTS, ACTIONS, SELF);

        // Then a KindnessEntry is saved,
        verify(mKindnessRepository).saveKindnessEntry(any(KindnessEntry.class));
        // ... and the kindness diary is displayed
        verify(mEditKindnessEntryView).showKindnessView();
    }

    @Test
    public void saveUpdatedEntryToRepository_SavesData() {
        // Given a valid timestamp
        mEditKindnessEntryPresenter.setCreationTimestamp(TIMESTAMP);

        // When the presenter is asked to open the entry
        mEditKindnessEntryPresenter.updateKindnessEntry(WORDS, THOUGHTS, ACTIONS, SELF);

        // Then a KindnessEntry is updated,
        verify(mKindnessRepository).updateKindnessEntry(any(KindnessEntry.class));
        // ... and the kindness diary is displayed
        verify(mEditKindnessEntryView).showKindnessView();
    }

}
