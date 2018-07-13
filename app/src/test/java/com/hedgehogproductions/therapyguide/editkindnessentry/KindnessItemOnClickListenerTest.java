package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.view.View;
import android.widget.RadioButton;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link KindnessItemOnClickListener}
 */

public class KindnessItemOnClickListenerTest {

    @Mock
    private KindnessEntry mKindnessEntry;

    @Mock
    private EditKindnessEntryArrayAdapter mAdapter;

    @Mock
    private EditKindnessEntryContract.View mView;

    @Mock
    private View mClickedView;

    @Mock
    private RadioButton mButton;

    @Before
    public void setupKindnessItemOnClickListener() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onClick_SetsCorrectKindnessAndMovesToNextPage() {
        when(mClickedView.findViewById(R.id.kindness_item_selector)).thenReturn(mButton);

        KindnessItemOnClickListener listener = new KindnessItemOnClickListener(
                2, 3, mKindnessEntry, mAdapter, mView);

        // When the item is clicked
        listener.onClick(mClickedView);

        // ... the radio button is found and toggled
        verify(mClickedView).findViewById(R.id.kindness_item_selector);
        verify(mButton).toggle();
        // ... the kindness is stored
        verify(mKindnessEntry).setActions(any(KindnessActions.class));
        // ... the next view is displayed.
        verify(mView).moveToNextView();
    }

    @Test
    public void lastPage_DoesNotMoveToNext() {
        when(mClickedView.findViewById(R.id.kindness_item_selector)).thenReturn(mButton);

        KindnessItemOnClickListener listener = new KindnessItemOnClickListener(
                3, 3, mKindnessEntry, mAdapter, mView);

        // When the item is clicked
        listener.onClick(mClickedView);

        // ... the radio button is found and toggled
        verify(mClickedView).findViewById(R.id.kindness_item_selector);
        verify(mButton).toggle();
        // ... the kindness is stored
        verify(mKindnessEntry).setSelf(any(KindnessSelf.class));
        // ... the next view is displayed.
        verifyZeroInteractions(mView);

    }
}
