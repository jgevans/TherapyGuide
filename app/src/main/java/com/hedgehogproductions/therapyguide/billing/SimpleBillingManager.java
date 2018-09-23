package com.hedgehogproductions.therapyguide.billing;

import android.app.Activity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.hedgehogproductions.therapyguide.listen.TrackStatusUpdatedListener;

import java.util.ArrayList;
import java.util.List;

public class SimpleBillingManager implements BillingManager, PurchasesUpdatedListener {

    private final static String TRACK_SKU_ID = "track_0001";
    private BillingClient mBillingClient;
    private boolean mBillingServiceConnected;
    private boolean mTrackPurchased;
    private TrackStatusUpdatedListener mListener;
    private String mTrackCost;
    private Activity mActivity;

    public SimpleBillingManager(Activity activity, TrackStatusUpdatedListener listener) {

        mBillingServiceConnected = false;
        mTrackPurchased = false;
        mListener = listener;
        mActivity = activity;
        mBillingClient = BillingClient.newBuilder(mActivity).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    mBillingServiceConnected = true;

                    // Update the price
                    List skuList = new ArrayList<String>();
                    skuList.add(TRACK_SKU_ID);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    mBillingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                    if (responseCode == BillingClient.BillingResponse.OK
                                            && skuDetailsList != null) {
                                        for (Object skuDetails : skuDetailsList) {
                                            String sku = ((SkuDetails) skuDetails).getSku();
                                            String price = ((SkuDetails) skuDetails).getPrice();
                                            if (TRACK_SKU_ID.equals(sku)) {
                                                mTrackCost = price;
                                                mListener.onTrackStatusUpdated();
                                            }
                                        }
                                    }
                                }
                            });

                    isTrackPurchased();
                    mListener.onTrackStatusUpdated();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                mBillingServiceConnected = false;
            }
        });
    }

    @Override
    public boolean isTrackPurchased() {
        if (mBillingServiceConnected) {
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
            if (purchasesResult.getResponseCode() == BillingClient.BillingResponse.OK &&
                    purchasesResult.getPurchasesList() != null) {
                for (Purchase purchase : purchasesResult.getPurchasesList()) {
                    // Process the result.
                    if (TRACK_SKU_ID.equals(purchase.getSku())) {
                        mTrackPurchased = true;
                    }
                }
            }
        }

        return mTrackPurchased;
    }

    @Override
    public String getTrackCost() {
        return mTrackCost;
    }

    @Override
    public void purchaseTrack() {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(TRACK_SKU_ID)
                .setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
                .build();
        mBillingClient.launchBillingFlow(mActivity, flowParams);
    }

    @Override
    public void onPurchasesUpdated(@BillingClient.BillingResponse int responseCode, List purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Object purchase : purchases) {
                if (TRACK_SKU_ID.equals(((Purchase)purchase).getSku())) {
                    mTrackPurchased = true;
                }
            }
        }
    }
}
