package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Context;
import android.support.annotation.NonNull;
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
    private EditKindnessEntryContract.UserActionsListener mActionsListener;
    private EditKindnessEntryArrayAdapter mWordsAdapter, mThoughtsAdapter, mActionsAdapter, mSelfAdapter;

    public EditKindnessEntryViewPagerAdapter( @NonNull Context context, @NonNull EditKindnessEntryContract.UserActionsListener actionsListener ) {
        mContext = context;
        mActionsListener = actionsListener;

        // Set up lists for listviews
        ArrayList<KindnessItem> kindnessWordsItems = new ArrayList<KindnessItem>();
        ArrayList<KindnessItem> kindnessThoughtsItems = new ArrayList<KindnessItem>();
        ArrayList<KindnessItem> kindnessActionsItems = new ArrayList<KindnessItem>();
        ArrayList<KindnessItem> kindnessSelfItems = new ArrayList<KindnessItem>();
        for (String text : context.getResources().getStringArray(R.array.kindness_words_array)) {
            kindnessWordsItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_thoughts_array)) {
            kindnessThoughtsItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_actions_array)) {
            kindnessActionsItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_self_array)) {
            kindnessSelfItems.add(new KindnessItem(text, false));
        }
        mWordsAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                kindnessWordsItems, 0, mActionsListener.getKindnessEntry());
        mThoughtsAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                kindnessThoughtsItems, 1, mActionsListener.getKindnessEntry());
        mActionsAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                kindnessActionsItems, 2, mActionsListener.getKindnessEntry());
        mSelfAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                kindnessSelfItems, 3, mActionsListener.getKindnessEntry());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(mViewList.get(position), container, false);
        container.addView(view);

        TextView kindnessInstruction = view.findViewById(R.id.kindness_instruction);
        ListView kindnessList = view.findViewById(R.id.kindness_list);
        kindnessList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    void addView(int view) {
        mViewList.add(view);
        notifyDataSetChanged();
    }
}
