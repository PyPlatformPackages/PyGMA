package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdRequest;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardItem;
import com.google.android.libraries.ads.mobile.sdk.common.AdError;

public class PyRewardedAd {
    private static final String TAG = "PyRewardedAd";
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
            RewardedAdRequest request = new RewardedAdRequest.Builder(mAdUnitId).build();

            RewardedAd.load(
                    mActivity,
                    request,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(RewardedAd rewardedAd) {
                            Log.d(TAG, "Rewarded Ad Loaded.");
                            mRewardedAd = rewardedAd;
                            rewardEarned = false;
                            adClosed = false;

                            mRewardedAd.setAdEventCallback(new RewardedAdEventCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    Log.d(TAG, "Rewarded Ad closed by user.");
                                    adClosed = true;
                                    mRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    Log.e(TAG, "Rewarded Ad failed to show.");
                                    adClosed = true;
                                    mRewardedAd = null;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(Exception error) {
                            Log.e(TAG, "Rewarded Ad Failed to Load.");
                            mRewardedAd = null;
                        }
                    });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mRewardedAd != null) {
                mRewardedAd.show(mActivity, (RewardItem rewardItem) -> {
                    Log.d(TAG, "User earned the reward!");
                    rewardEarned = true;
                });
            } else {
                Log.w(TAG, "The rewarded ad wasn't loaded yet.");
            }
        });
    }

    public boolean isLoaded() {
        return mRewardedAd != null;
    }

    public boolean checkAndResetReward() {
        boolean earned = rewardEarned;
        rewardEarned = false;
        return earned;
    }

    public boolean checkAndResetAdClosed() {
        if (adClosed) {
            adClosed = false;
            return true;
        }
        return false;
    }
}