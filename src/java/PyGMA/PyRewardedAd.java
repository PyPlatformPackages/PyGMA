package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardItem;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError;

public class PyRewardedAd {
    private RewardedAd mRewardedAd;
    private Activity mActivity;
    private String mAdUnitId;

    private boolean rewardEarned = false;
    private boolean adClosed = false;

    public PyRewardedAd(Activity activity, String adUnitId) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
    }

    public void loadAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            // FIX: Removed mActivity context parameter
            RewardedAd.load(
                    request,
                    new AdLoadCallback<RewardedAd>() {
                        @Override
                        public void onAdLoaded(RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            rewardEarned = false;
                            adClosed = false;

                            mRewardedAd.setAdEventCallback(new RewardedAdEventCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    adClosed = true;
                                    mRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                                    adClosed = true;
                                    mRewardedAd = null;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError error) {
                            mRewardedAd = null;
                        }
                    });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mRewardedAd != null) {
                mRewardedAd.show(mActivity, (RewardItem rewardItem) -> {
                    rewardEarned = true;
                });
            }
        });
    }

    public boolean isLoaded() {
        return mRewardedAd != null;
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