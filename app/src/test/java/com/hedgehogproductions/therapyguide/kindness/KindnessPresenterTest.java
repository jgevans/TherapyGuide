package com.hedgehogproductions.therapyguide.kindness;

import com.google.common.collect.Lists;
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

import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link KindnessPresenter}
 */
public class KindnessPresenterTest {

    private static final List<KindnessEntry> KINDNESSDIARY = Lists.newArrayList(
            new KindnessEntry(System.currentTimeMillis(), KindnessWords.LOVE, KindnessThoughts.SUCCESS,
                    KindnessActions.TRAFFIC, KindnessSelf.MOVIE, true),
            new KindnessEntry(System.currentTimeMillis(), KindnessWords.ABILITIES, KindnessThoughts.GOSSIP,
                    KindnessActions.CAKE, KindnessSelf.FRIEND, false));


    @Mock
    private KindnessRepository mKindnessRepository;

    @Mock
    private KindnessContract.View mKindnessView;


    private KindnessPresenter mKindnessPresenter;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<KindnessRepository.LoadKindnessCallback> mLoadDiaryCallbackCaptor;


    @Before
    public void setupKindnessPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mKindnessPresenter = new KindnessPresenter(mKindnessRepository, mKindnessView);
    }

    @Test
    public void loadDiaryFromRepositoryAndLoadIntoView() {
        // Given an initialized DiaryPresenter with initialised diary entries
        // When loading of Diary is requested
        mKindnessPresenter.loadKindnessDiary();

        // The callback is captured and invoked with stubbed notes
        verify(mKindnessRepository).getKindnessDiary(mLoadDiaryCallbackCaptor.capture());
        mLoadDiaryCallbackCaptor.getValue().onKindnessLoaded(KINDNESSDIARY);

        // Then the diary entries are shown in the UI
        verify(mKindnessView).showKindnessDiary(KINDNESSDIARY);
    }

    @Test
    public void clickOnFab_ShowsAddDiaryEntryUi() {
        // When adding a new diary entry
        mKindnessPresenter.addNewKindnessEntry();

        // Then add diary entry UI is shown
        verify(mKindnessView).showAddKindnessEntry();
    }

    @Test
    public void instigateDeleteEntryRequest_ShowsDeletionMessage() {
        final int position = 3;

        // When trying to start deleting an entry
        mKindnessPresenter.instigateKindnessEntryDeletion(position);

        // Then deletion confirmation is shown
        verify(mKindnessView).showKindnessEntryDeletionMessage(position);
    }

    @Test
    public void deleteEntry_RemovesFromRepo() {
        // When asked to delete an entry
        mKindnessPresenter.deleteKindnessEntry(KINDNESSDIARY.get(0));

        // The deletion is sent to the repository
        verify(mKindnessRepository).deleteKindnessEntry(KINDNESSDIARY.get(0));
    }
}
