package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;

import com.hedgehogproductions.therapyguide.R;

public enum KindnessActions {
    DOOR, ASSIST, TRAFFIC, WALK, LITTER, CAKE, HELP, THANKS, BUY, OTHER;

    public String toString(Context context) {
        switch (this) {
            case DOOR:
                return context.getString(R.string.kindness_actions_door);
            case ASSIST:
                return context.getString(R.string.kindness_actions_assist);
            case TRAFFIC:
                return context.getString(R.string.kindness_actions_traffic);
            case WALK:
                return context.getString(R.string.kindness_actions_walk);
            case LITTER:
                return context.getString(R.string.kindness_actions_litter);
            case CAKE:
                return context.getString(R.string.kindness_actions_cake);
            case HELP:
                return context.getString(R.string.kindness_actions_help);
            case THANKS:
                return context.getString(R.string.kindness_actions_thanks);
            case BUY:
                return context.getString(R.string.kindness_actions_buy);
            case OTHER:
                return context.getString(R.string.kindness_actions_other);
            default:
                throw new IllegalArgumentException();
        }
    }
}
