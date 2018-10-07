package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;

import com.hedgehogproductions.therapyguide.R;

public enum KindnessCategories{
    WORDS, THOUGHTS, ACTIONS, SELF, NONE;

    public String toString(Context context) {
        switch (this) {
            case NONE:
                return "Not selected";
            case WORDS:
                return context.getString(R.string.kindness_categories_words);
            case THOUGHTS:
                return context.getString(R.string.kindness_categories_thoughts);
            case ACTIONS:
                return context.getString(R.string.kindness_categories_actions);
            case SELF:
                return context.getString(R.string.kindness_categories_self);
            default:
                throw new IllegalArgumentException();
        }

    }
}
