package com.hedgehogproductions.therapyguide.kindnessdata;

import android.os.Build;

import java.util.Objects;

public class KindnessEntry {

    private long mCreationTimestamp = ~0;
    private KindnessWords mWords = null;
    private KindnessThoughts mThoughts = null;
    private KindnessActions mActions = null;
    private KindnessSelf mSelf = null;

    private boolean mComplete;

    public KindnessEntry() {}

    public KindnessEntry(long timestamp, KindnessWords words, KindnessThoughts thoughts,
                         KindnessActions actions, KindnessSelf self) {
        this(timestamp, words, thoughts, actions, self, false);
    }

    public KindnessEntry(long timestamp, KindnessWords words, KindnessThoughts thoughts,
                         KindnessActions actions, KindnessSelf self, boolean complete) {
        mCreationTimestamp = timestamp;
        mWords = words;
        mThoughts = thoughts;
        mActions = actions;
        mSelf = self;
        mComplete = complete;
    }

    public long getCreationTimestamp() {
        return mCreationTimestamp;
    }

    public KindnessWords getWords() {
        return mWords;
    }
    public KindnessThoughts getThoughts() {
        return mThoughts;
    }
    public KindnessActions getActions() {
        return mActions;
    }
    public KindnessSelf getSelf() {
        return mSelf;
    }

    public void setWords(KindnessWords words) {
        mWords = words;
    }
    public void setThoughts(KindnessThoughts thoughts) {
        mThoughts = thoughts;
    }
    public void setActions(KindnessActions actions) {
        mActions = actions;
    }
    public void setSelf(KindnessSelf self) {
        mSelf = self;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public boolean isEmpty() {
        return mCreationTimestamp == 0 || mCreationTimestamp == ~0 ||
                mWords == null || mThoughts == null || mActions == null || mSelf == null;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (o instanceof KindnessEntry) {
            KindnessEntry user = (KindnessEntry) o;
            return mCreationTimestamp == user.mCreationTimestamp;
        }
        else {
            return o instanceof Long && mCreationTimestamp == (Long) o;
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(mCreationTimestamp);
        }
        else {
            return ((int) mCreationTimestamp);
        }
    }

    @Override
    public String toString() {
        String words, thoughts, actions, self;
        if( mWords == null ) {
            words = "(Not Chosen)";
        } else {
            words = mWords.toString();
        }
        if( mThoughts == null ) {
            thoughts = "(Not Chosen)";
        } else {
            thoughts = mThoughts.toString();
        }
        if( mActions == null ) {
            actions = "(Not Chosen)";
        } else {
            actions = mActions.toString();
        }
        if( mSelf == null ) {
            self = "(Not Chosen)";
        } else {
            self = mSelf.toString();
        }
        return String.valueOf(mCreationTimestamp + ": " + words + ","
                + thoughts + "," + actions + "," + self);
    }
}
