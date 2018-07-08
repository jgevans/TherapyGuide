package com.hedgehogproductions.therapyguide.kindnessdata;

import android.os.Build;

import java.util.Date;
import java.util.Objects;

public class KindnessEntry {

    private Date mCreationDate = null;
    private KindnessWords mWords = null;
    private KindnessThoughts mThoughts = null;
    private KindnessActions mActions = null;
    private KindnessSelf mSelf = null;

    private boolean mComplete;

    public KindnessEntry() {}

    public KindnessEntry(Date date) {
        mCreationDate = date;
    }

    public KindnessEntry(Date date, KindnessWords words, KindnessThoughts thoughts,
                         KindnessActions actions, KindnessSelf self) {
        this(date, words, thoughts, actions, self, false);
    }

    public KindnessEntry(Date date, KindnessWords words, KindnessThoughts thoughts,
                         KindnessActions actions, KindnessSelf self, boolean complete) {
        mCreationDate = date;
        mWords = words;
        mThoughts = thoughts;
        mActions = actions;
        mSelf = self;
        mComplete = complete;
    }

    public Date getCreationDate() {
        return mCreationDate;
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
        return mCreationDate == null ||
                mWords == null || mThoughts == null || mActions == null || mSelf == null;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (o instanceof KindnessEntry) {
            KindnessEntry user = (KindnessEntry) o;
            return mCreationDate.equals(user.mCreationDate);
        }
        else {
            return o instanceof Date && mCreationDate.equals(o);
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(mCreationDate);
        }
        else {
            return ((int) mCreationDate.getTime());
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
        return String.valueOf(mCreationDate + ": " + words + ","
                + thoughts + "," + actions + "," + self);
    }
}
