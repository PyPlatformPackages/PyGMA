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
    private boolean adClosed = false;

    public PyInterstitialAd(Activity activity, String adUnitId) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
    }

    public void loadAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            InterstitialAd.load(
                    mActivity,
                    request,
                    new AdLoadCallback<InterstitialAd>() {
                        @Override
                        public void onAdLoaded(InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            adClosed = false;

                            mInterstitialAd.setAdEventCallback(new InterstitialAdEventCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    adClosed = true;
                                    mInterstitialAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                                    adClosed = true;
                                    mInterstitialAd = null;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError error) {
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

    public boolean checkAndResetAdClosed() {
        if (adClosed) {
            adClosed = false;
            return true;
        }
        return false;
    }
}