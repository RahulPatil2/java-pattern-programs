package com.java.pattern.program.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.java.pattern.program.R;
import com.java.pattern.program.data.constant.AppConstant;

public class AdsUtilities {

    private static AdsUtilities mAdsUtilities;

    private InterstitialAd mInterstitialAd;

    private boolean mDisableBannerAd = false, mDisableInterstitialAd = false;

    private static int mClickCount = 0;

    private AdsUtilities(Context context) {
        MobileAds.initialize(context, initializationStatus -> {});
    }

    public static AdsUtilities getInstance(Context context) {
        if (mAdsUtilities == null) {
            mAdsUtilities = new AdsUtilities(context);
        }
        return mAdsUtilities;
    }

    public void showBannerAd(final AdView mAdView) {
        if (mDisableBannerAd) {
            mAdView.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    mAdView.setVisibility(View.GONE);
                }
            });
        }
    }

    public void loadFullScreenAd(Activity activity) {
        if (!mDisableInterstitialAd) {
            mClickCount++;
            if (mClickCount >= AppConstant.CLICK_COUNT) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, activity.getString(R.string.interstitial_ad_unit_id), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }
                });
            }
        }
    }

    public boolean showFullScreenAd(Activity activity) {
        if (!mDisableInterstitialAd) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
                mClickCount = 0;
                mInterstitialAd = null;
                return true;
            }
        }
        return false;
    }

    public void disableBannerAd() {
        this.mDisableBannerAd = true;
    }

    public void disableInterstitialAd() {
        this.mDisableInterstitialAd = true;
    }
}