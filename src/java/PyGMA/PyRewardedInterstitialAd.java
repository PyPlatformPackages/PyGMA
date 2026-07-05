package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardItem;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError;

public class PyRewardedInterstitialAd {
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
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            RewardedInterstitialAd.load(
                    mActivity, request,
                    new AdLoadCallback<RewardedInterstitialAd>() {
                        @Override
                        public void onAdLoaded(RewardedInterstitialAd ad) {
                            mAd = ad;
                            rewardEarned = false;
                            adClosed = false;

                            mAd.setAdEventCallback(new RewardedInterstitialAdEventCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    adClosed = true;
                                    mAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                                    adClosed = true;
                                    mAd = null;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError error) {
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