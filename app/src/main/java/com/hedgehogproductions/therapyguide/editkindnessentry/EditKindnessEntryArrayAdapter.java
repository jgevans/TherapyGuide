package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class EditKindnessEntryArrayAdapter extends ArrayAdapter<String> {
    public EditKindnessEntryArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] kindnessItems) {
        super(context, resource, textViewResourceId, kindnessItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
