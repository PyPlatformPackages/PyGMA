package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardItem;
import com.google.android.libraries.ads.mobile.sdk.common.AdError;
import com.google.android.libraries.ads.mobile.sdk.FullScreenContentCallback;
import com.google.android.libraries.ads.mobile.sdk.adrequest.AdRequest;

public class PyRewardedInterstitialAd {
    private static final String TAG = "PyRewardedInterAd";
    private RewardedInterstitialAd mAd;
    private Activity mActivity;
    private String mAdUnitId;

    private boolean rewardEarned = false;
    private boolean adClosed = false;

    public PyRewardedInterstitialAd(Activity activity, String adUnitId) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
    }

    public void loadAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder().build();

            RewardedInterstitialAd.load(
                    mActivity, mAdUnitId, request,
                    new RewardedInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(RewardedInterstitialAd ad) {
                            Log.d(TAG, "Rewarded Interstitial Loaded.");
                            mAd = ad;
                            rewardEarned = false;
                            adClosed = false;

                            mAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    adClosed = true;
                                    mAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    adClosed = true;
                                    mAd = null;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(Exception error) {
                            mAd = null;
                        }
                    });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mAd != null) {
                mAd.show(mActivity, (RewardItem rewardItem) -> {
                    rewardEarned = true;
                });
            }
        });
    }

    public boolean isLoaded() {
        return mAd != null;
    }

    public boolean checkAndResetReward() {
        boolean e = rewardEarned;
        rewardEarned = false;
        return e;
    }

    public boolean checkAndResetAdClosed() {
        if (adClosed) {
            adClosed = false;
            return true;
        }
        return false;
    }
}