package com.hedgehogproductions.therapyguide.intro;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class IntroViewPagerAdapter extends PagerAdapter {
    private final List<Integer> mViewList = new ArrayList<>();
    private final Context mContext;

    public IntroViewPagerAdapter( Context context ) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(mViewList.get(position), container, false);
        container.addView(view);

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
