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
    private PyGMAListener mListener;

    public PyRewardedInterstitialAd(Activity activity, String adUnitId, PyGMAListener listener) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
        this.mListener = listener;
    }

    public void loadAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            RewardedInterstitialAd.load(request, new AdLoadCallback<RewardedInterstitialAd>() {
                @Override
                public void onAdLoaded(RewardedInterstitialAd ad) {
                    mAd = ad;
                    if (mListener != null)
                        mListener.onAdLoaded();

                    mAd.setAdEventCallback(new RewardedInterstitialAdEventCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            if (mListener != null)
                                mListener.onAdClosed();
                            mAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                            if (mListener != null)
                                mListener.onAdFailed(error.getMessage());
                            mAd = null;
                        }

                        @Override
                        public void onAdClicked() {
                            if (mListener != null)
                                mListener.onAdClicked();
                        }

                        @Override
                        public void onAdImpression() {
                            if (mListener != null)
                                mListener.onAdImpression();
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(LoadAdError error) {
                    if (mListener != null)
                        mListener.onAdFailed(error.getMessage());
                    mAd = null;
                }
            });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mAd != null) {
                mAd.show(mActivity, (RewardItem rewardItem) -> {
                    if (mListener != null)
                        mListener.onRewardEarned();
                });
            }
        });
    }

    public boolean isLoaded() {
        return mAd != null;
    }
}