package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessCategories;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import java.util.ArrayList;

class EditKindnessEntryArrayAdapter extends ArrayAdapter<KindnessItem> {

    private final int mPage;
    private final KindnessEntry mKindnessEntry;
    private final EditKindnessEntryContract.View mEditKindnessEntryView;
    private final Context mContext;

    EditKindnessEntryArrayAdapter(@NonNull Context context, int resource, int textViewResourceId,
                                  @NonNull ArrayList<KindnessItem> kindnessItems, int page,
                                  @NonNull KindnessEntry entry, @NonNull EditKindnessEntryContract.View view) {
        super(context, resource, textViewResourceId, kindnessItems);
        mPage = page;
        mKindnessEntry = entry;
        mEditKindnessEntryView = view;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        KindnessItem item = getItem(position);
        String text = item.getText();
        boolean selected = item.isSelected();

        // Check if this is the currently selected item
        switch(mPage) {
            case 0:
                if (mKindnessEntry.getCategory() != null && mKindnessEntry.getCategory() == KindnessCategories.values()[position] ) {
                    selected = true;
                }
                break;
            case 1:
                switch (mKindnessEntry.getCategory()) {
                    case WORDS:
                        if (mKindnessEntry.getValue() != null && mKindnessEntry.getValue().equals(KindnessWords.values()[position].toString(mContext))) {
                            selected = true;
                        }
                        break;
                    case THOUGHTS:
                        if (mKindnessEntry.getValue() != null && mKindnessEntry.getValue().equals(KindnessThoughts.values()[position].toString(mContext))) {
                            selected = true;
                        }
                        break;
                    case ACTIONS:
                        if (mKindnessEntry.getValue() != null && mKindnessEntry.getValue().equals(KindnessActions.values()[position].toString(mContext))) {
                            selected = true;
                        }
                        break;
                    case SELF:
                        if (mKindnessEntry.getValue() != null && mKindnessEntry.getValue().equals(KindnessSelf.values()[position].toString(mContext))) {
                            selected = true;
                        }
                        break;
                    default:
                }
                break;
            default:
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.kindness_item, parent, false);
        }

        TextView kindnessText = convertView.findViewById(R.id.kindness_item_text);
        RadioButton kindnessSelector = convertView.findViewById(R.id.kindness_item_selector);

        convertView.setClickable(true);
        convertView.setFocusable(true);
        convertView.setOnClickListener(new KindnessItemOnClickListener(mPage, position,
                mKindnessEntry, this, mEditKindnessEntryView, mContext));

        kindnessText.setText(text);
        kindnessSelector.setChecked(selected);

        return convertView;
    }
}
