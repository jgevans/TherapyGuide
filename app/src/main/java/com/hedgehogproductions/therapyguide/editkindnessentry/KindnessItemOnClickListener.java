package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

public class KindnessItemOnClickListener implements View.OnClickListener {

    private int mPage, mPosition;

    private KindnessEntry mKindnessEntry;

    private EditKindnessEntryArrayAdapter mAdapter;

    KindnessItemOnClickListener(int page, int position, @NonNull KindnessEntry entry, EditKindnessEntryArrayAdapter adapter ) {
        mPage = page;
        mPosition = position;
        mKindnessEntry = entry;
        mAdapter = adapter;
    }

    @Override
    public void onClick(View v) {
        ((RadioButton) v.findViewById(R.id.kindness_item_selector)).toggle();
        switch( mPage ) {
            case 0:
                mKindnessEntry.setWords(KindnessWords.values()[mPosition]);
                break;
            case 1:
                mKindnessEntry.setThoughts(KindnessThoughts.values()[mPosition]);
                break;
            case 2:
                mKindnessEntry.setActions(KindnessActions.values()[mPosition]);
                break;
            case 3:
                mKindnessEntry.setSelf(KindnessSelf.values()[mPosition]);
                break;
            default:
                throw new ArrayIndexOutOfBoundsException("Too many Kindness types");
        }
        v.post(new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
        if( mPage != 3 ) {
            // Unless on last page, move to next
            v.getRootView().findViewById(R.id.editkindnessentry_next_button).performClick();
        }
    }
}
