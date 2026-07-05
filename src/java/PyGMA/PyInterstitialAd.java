package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd;
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError;

public class PyInterstitialAd {
    private InterstitialAd mInterstitialAd;
    private Activity mActivity;
    private String mAdUnitId;
    private PyGMAListener mListener;

    public PyInterstitialAd(Activity activity, String adUnitId, PyGMAListener listener) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
        this.mListener = listener;
    }

    public void loadAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            InterstitialAd.load(request, new AdLoadCallback<InterstitialAd>() {
                @Override
                public void onAdLoaded(InterstitialAd interstitialAd) {
                    mInterstitialAd = interstitialAd;
                    if (mListener != null)
                        mListener.onAdLoaded();

                    mInterstitialAd.setAdEventCallback(new InterstitialAdEventCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            if (mListener != null)
                                mListener.onAdClosed();
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                            if (mListener != null)
                                mListener.onAdFailed(error.getMessage());
                            mInterstitialAd = null;
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
                    mInterstitialAd = null;
                }
            });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(mActivity);
            }
        });
    }

    public boolean isLoaded() {
        return mInterstitialAd != null;
    }
}