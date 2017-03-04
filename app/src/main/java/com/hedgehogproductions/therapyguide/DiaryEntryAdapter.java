package com.hedgehogproductions.therapyguide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DiaryEntryAdapter extends RecyclerView.Adapter<DiaryEntryAdapter.ViewHolder> {

    private final List<DiaryEntry> mDiary;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView time;


        public ViewHolder(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.diary_text);
            time = (TextView) view.findViewById(R.id.diary_time);

        }
    }


    public DiaryEntryAdapter(Context context, List<DiaryEntry> diary) {
        mDiary = diary;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DiaryEntry entry = mDiary.get(position);
        holder.text.setText(entry.getText());
        holder.time.setText(String.valueOf(entry.getCreationTimestamp()));
    }

    @Override
    public int getItemCount() {
        return mDiary.size();
    }
}
