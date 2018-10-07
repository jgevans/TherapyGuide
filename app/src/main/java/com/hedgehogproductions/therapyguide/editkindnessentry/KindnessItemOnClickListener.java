package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessCategories;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

class KindnessItemOnClickListener implements View.OnClickListener {

    private final int mPage, mPosition;

    private final KindnessEntry mKindnessEntry;

    private final EditKindnessEntryArrayAdapter mAdapter;

    private final EditKindnessEntryContract.View mEditKindnessEntryView;

    private final Context mContext;

    KindnessItemOnClickListener(int page, int position, @NonNull KindnessEntry entry,
                                EditKindnessEntryArrayAdapter adapter, EditKindnessEntryContract.View view,
                                Context context ) {
        mPage = page;
        mPosition = position;
        mKindnessEntry = entry;
        mAdapter = adapter;
        mEditKindnessEntryView = view;
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        ((RadioButton) v.findViewById(R.id.kindness_item_selector)).toggle();
        switch( mPage ) {
            case 0:
                mKindnessEntry.setCategory(KindnessCategories.values()[mPosition]);

                break;
            case 1:
                switch( mKindnessEntry.getCategory() ) {
                    case NONE:
                        break;
                    case WORDS:
                        mKindnessEntry.setValue((KindnessWords.values()[mPosition]).toString(mContext));
                        break;
                    case THOUGHTS:
                        mKindnessEntry.setValue((KindnessThoughts.values()[mPosition]).toString(mContext));
                        break;
                    case ACTIONS:
                        mKindnessEntry.setValue((KindnessActions.values()[mPosition]).toString(mContext));
                        break;
                    case SELF:
                        mKindnessEntry.setValue((KindnessSelf.values()[mPosition]).toString(mContext));
                        break;
                    default:
                }
                break;
            default:
                throw new ArrayIndexOutOfBoundsException("No page at position " + mPage);
        }
        v.post(new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
        if( mPage != 1 ) {
            // Unless on last page, move to next
            mEditKindnessEntryView.moveToNextView(mKindnessEntry.getCategory());
        }
    }
}
