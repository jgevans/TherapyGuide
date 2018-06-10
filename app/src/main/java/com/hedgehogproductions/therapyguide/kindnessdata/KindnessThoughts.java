package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;

import com.hedgehogproductions.therapyguide.R;

public enum KindnessThoughts {
    WISHES, DOUBT, FORGIVE, SUCCESS, LISTEN, GOSSIP, OTHER;

    public String toString(Context context) {
        switch (this) {
            case WISHES:
                return context.getString(R.string.kindness_thoughts_wishes);
            case DOUBT:
                return context.getString(R.string.kindness_thoughts_doubt);
            case FORGIVE:
                return context.getString(R.string.kindness_thoughts_forgive);
            case SUCCESS:
                return context.getString(R.string.kindness_thoughts_success);
            case LISTEN:
                return context.getString(R.string.kindness_thoughts_listen);
            case GOSSIP:
                return context.getString(R.string.kindness_thoughts_gossip);
            case OTHER:
                return context.getString(R.string.kindness_thoughts_other);
            default:
                throw new IllegalArgumentException();
        }
    }
}