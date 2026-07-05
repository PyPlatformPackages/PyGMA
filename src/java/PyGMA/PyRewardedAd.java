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
    private PyGMAListener mListener;

    public PyRewardedAd(Activity activity, String adUnitId, PyGMAListener listener) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
        this.mListener = listener;
    }

    public void loadAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            RewardedAd.load(request, new AdLoadCallback<RewardedAd>() {
                @Override
                public void onAdLoaded(RewardedAd rewardedAd) {
                    mRewardedAd = rewardedAd;
                    if (mListener != null)
                        mListener.onAdLoaded();

                    mRewardedAd.setAdEventCallback(new RewardedAdEventCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            if (mListener != null)
                                mListener.onAdClosed();
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                            if (mListener != null)
                                mListener.onAdFailed(error.getMessage());
                            mRewardedAd = null;
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(LoadAdError error) {
                    if (mListener != null)
                        mListener.onAdFailed(error.getMessage());
                    mRewardedAd = null;
                }
            });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mRewardedAd != null) {
                mRewardedAd.show(mActivity, (RewardItem rewardItem) -> {
                    if (mListener != null)
                        mListener.onRewardEarned();
                });
            }
        });
    }

    public boolean isLoaded() {
        return mRewardedAd != null;
    }
}