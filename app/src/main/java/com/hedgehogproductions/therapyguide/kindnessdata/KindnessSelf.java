package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;

import com.hedgehogproductions.therapyguide.R;

public enum KindnessSelf {
    COMPLIMENT, COMPASSION, FRIEND, DAY, CALL, GENEROUS, FORGIVE, KIND, ACCEPT, CLASS, VOLUNTEER,
    MOVIE, ACTIVE, HOBBY, WALK, COOK, DATE, NATURE, READ, LAUGH, DRAW, OTHER;

    public String toString(Context context) {
        switch (this) {
            case COMPLIMENT:
                return context.getString(R.string.kindness_self_compliment);
            case COMPASSION:
                return context.getString(R.string.kindness_self_compassion);
            case FRIEND:
                return context.getString(R.string.kindness_self_friend);
            case DAY:
                return context.getString(R.string.kindness_self_day);
            case CALL:
                return context.getString(R.string.kindness_self_call);
            case GENEROUS:
                return context.getString(R.string.kindness_self_generous);
            case FORGIVE:
                return context.getString(R.string.kindness_self_forgive);
            case KIND:
                return context.getString(R.string.kindness_self_kind);
            case ACCEPT:
                return context.getString(R.string.kindness_self_accept);
            case CLASS:
                return context.getString(R.string.kindness_self_class);
            case VOLUNTEER:
                return context.getString(R.string.kindness_self_volunteer);
            case MOVIE:
                return context.getString(R.string.kindness_self_movie);
            case ACTIVE:
                return context.getString(R.string.kindness_self_active);
            case HOBBY:
                return context.getString(R.string.kindness_self_hobby);
            case WALK:
                return context.getString(R.string.kindness_self_walk);
            case COOK:
                return context.getString(R.string.kindness_self_cook);
            case DATE:
                return context.getString(R.string.kindness_self_date);
            case NATURE:
                return context.getString(R.string.kindness_self_nature);
            case READ:
                return context.getString(R.string.kindness_self_read);
            case LAUGH:
                return context.getString(R.string.kindness_self_laugh);
            case DRAW:
                return context.getString(R.string.kindness_self_draw);
            case OTHER:
                return context.getString(R.string.kindness_self_other);
            default:
                throw new IllegalArgumentException();
        }
    }
}
