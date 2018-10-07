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
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessCategories;

import java.util.ArrayList;
import java.util.List;

class EditKindnessEntryViewPagerAdapter extends PagerAdapter {
    private final List<Integer> mViewList = new ArrayList<>();
    private final Context mContext;
    private final ArrayList<KindnessItem> mKindnessValuesItems, mKindnessWordsItems,
            mKindnessThoughtsItems, mKindnessActionsItems, mKindnessSelfItems;
    private final EditKindnessEntryArrayAdapter mCategoriesAdapter, mValuesAdapter;

    private KindnessCategories mKindnessCategory;

    EditKindnessEntryViewPagerAdapter( @NonNull Context context, @NonNull EditKindnessEntryContract.UserActionsListener actionsListener,
                                       @NonNull EditKindnessEntryContract.View view) {
        mContext = context;
        mKindnessCategory = KindnessCategories.NONE;

        // Set up lists for ListViews
        ArrayList<KindnessItem> kindnessCategoriesItems = new ArrayList<>();
        mKindnessValuesItems = new ArrayList<>();
        mKindnessWordsItems = new ArrayList<>();
        mKindnessThoughtsItems = new ArrayList<>();
        mKindnessActionsItems = new ArrayList<>();
        mKindnessSelfItems = new ArrayList<>();
        for (String text : context.getResources().getStringArray(R.array.kindness_categories_array)) {
            kindnessCategoriesItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_words_array)) {
            mKindnessWordsItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_thoughts_array)) {
            mKindnessThoughtsItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_actions_array)) {
            mKindnessActionsItems.add(new KindnessItem(text, false));
        }
        for (String text : context.getResources().getStringArray(R.array.kindness_self_array)) {
            mKindnessSelfItems.add(new KindnessItem(text, false));
        }
        mCategoriesAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                kindnessCategoriesItems, 0, actionsListener.getKindnessEntry(), view);
        mValuesAdapter = new EditKindnessEntryArrayAdapter(context,
                R.layout.kindness_item,
                R.id.kindness_item_text,
                mKindnessValuesItems, 1, actionsListener.getKindnessEntry(), view);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(mViewList.get(position), container, false);
        container.addView(view);

        TextView kindnessInstruction = view.findViewById(R.id.kindness_instruction);
        ListView kindnessList = view.findViewById(R.id.kindness_list);
        kindnessList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        switch (position) {
            case 0:
                kindnessInstruction.setText(R.string.kindness_create_categories_message);
                kindnessList.setAdapter(mCategoriesAdapter);
                break;
            case 1:
                kindnessList.setAdapter(mValuesAdapter);
                switch (mKindnessCategory) {
                    case NONE:
                        kindnessInstruction.setText(R.string.kindness_create_categories_please_message);
                        break;
                    case WORDS:
                        kindnessInstruction.setText(R.string.kindness_create_words_message);
                        break;
                    case THOUGHTS:
                        kindnessInstruction.setText(R.string.kindness_create_thoughts_message);
                        break;
                    case ACTIONS:
                        kindnessInstruction.setText(R.string.kindness_create_actions_message);
                        break;
                    case SELF:
                        kindnessInstruction.setText(R.string.kindness_create_self_message);
                        break;
                    default:
                        // not yet selected.
                }
                break;
            default:
                throw new ArrayIndexOutOfBoundsException("No view at position " + position);
        }
        return view;
    }

    // Override to force re-instantiation of value item on data change
    @Override
    public int getItemPosition(@NonNull Object item) {
        return POSITION_NONE;
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

    void addViews() {
        mViewList.add(R.layout.editkindnessentry_section);
        mViewList.add(R.layout.editkindnessentry_section);
        notifyDataSetChanged();
    }

    void setKindnessView(KindnessCategories kindnessCategory) {
        mKindnessCategory = kindnessCategory;
        switch (mKindnessCategory) {
            case WORDS:
                mValuesAdapter.clear();
                mValuesAdapter.addAll(mKindnessWordsItems);
                break;
            case THOUGHTS:
                mValuesAdapter.clear();
                mValuesAdapter.addAll(mKindnessThoughtsItems);
                break;
            case ACTIONS:
                mValuesAdapter.clear();
                mValuesAdapter.addAll(mKindnessActionsItems);
                break;
            case SELF:
                mValuesAdapter.clear();
                mValuesAdapter.addAll(mKindnessSelfItems);
                break;
            case NONE:
                mKindnessValuesItems.clear();
        }
        notifyDataSetChanged();
    }
}
