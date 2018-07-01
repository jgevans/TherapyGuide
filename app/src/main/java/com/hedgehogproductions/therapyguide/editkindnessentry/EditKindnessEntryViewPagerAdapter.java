package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hedgehogproductions.therapyguide.R;

import java.util.ArrayList;
import java.util.List;

public class EditKindnessEntryViewPagerAdapter extends PagerAdapter {
    private final List<Integer> mViewList = new ArrayList<>();
    private final Context mContext;
    private EditKindnessEntryArrayAdapter mWordsAdapter, mThoughtsAdapter, mActionsAdapter, mSelfAdapter;

    public EditKindnessEntryViewPagerAdapter(Context context ) {
        mContext = context;

        // Set up lists for listviews
        mWordsAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                context.getResources().getStringArray(R.array.kindness_words_array));
        mThoughtsAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                context.getResources().getStringArray(R.array.kindness_thoughts_array));
        mActionsAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                context.getResources().getStringArray(R.array.kindness_actions_array));
        mSelfAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                context.getResources().getStringArray(R.array.kindness_self_array));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(mViewList.get(position), container, false);
        container.addView(view);

        TextView kindnessInstruction = view.findViewById(R.id.kindness_instruction);
        ListView kindnessList = view.findViewById(R.id.kindness_list);
        switch (position) {
            case 0:
                kindnessInstruction.setText(R.string.kindness_create_words_message);
                kindnessList.setAdapter(mWordsAdapter);
                break;
            case 1:
                kindnessInstruction.setText(R.string.kindness_create_thoughts_message);
                kindnessList.setAdapter(mThoughtsAdapter);
                break;
            case 2:
                kindnessInstruction.setText(R.string.kindness_create_actions_message);
                kindnessList.setAdapter(mActionsAdapter);
                break;
            case 3:
                kindnessInstruction.setText(R.string.kindness_create_self_message);
                kindnessList.setAdapter(mSelfAdapter);
                break;
            default:
                throw new ArrayIndexOutOfBoundsException("Too many Kindness types");

        }

        return view;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    void addView(int view) {
        mViewList.add(view);
        notifyDataSetChanged();
    }
}
