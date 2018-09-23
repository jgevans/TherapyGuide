package com.hedgehogproductions.therapyguide.billing;

public interface BillingManager {

    String getTrackCost();

    boolean isTrackPurchased();

    void purchaseTrack();
}
