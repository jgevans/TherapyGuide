package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;

import com.hedgehogproductions.therapyguide.R;

public enum KindnessWords {
    APPEARANCE, SKILLS, ABILITIES, LOVE, THANKS, CALL, OTHER;

    public String toString(Context context) {
        switch (this) {
            case APPEARANCE:
                return context.getString(R.string.kindness_words_appearance);
            case SKILLS:
                return context.getString(R.string.kindness_words_skills);
            case ABILITIES:
                return context.getString(R.string.kindness_words_abilities);
            case LOVE:
                return context.getString(R.string.kindness_words_love);
            case THANKS:
                return context.getString(R.string.kindness_words_thanks);
            case CALL:
                return context.getString(R.string.kindness_words_call);
            case OTHER:
                return context.getString(R.string.kindness_words_other);
            default:
                throw new IllegalArgumentException();
        }

    }
}
