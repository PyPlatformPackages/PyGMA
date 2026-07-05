package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd;
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdRequest;
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.common.AdError;

public class PyInterstitialAd {
    private static final String TAG = "PyInterstitialAd";
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
            InterstitialAdRequest request = new InterstitialAdRequest.Builder(mAdUnitId).build();

            InterstitialAd.load(
                    mActivity,
                    request,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(InterstitialAd interstitialAd) {
                            Log.d(TAG, "Interstitial Ad Loaded.");
                            mInterstitialAd = interstitialAd;
                            adClosed = false;

                            mInterstitialAd.setAdEventCallback(new InterstitialAdEventCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    Log.d(TAG, "Ad closed by user.");
                                    adClosed = true;
                                    mInterstitialAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    Log.e(TAG, "Ad failed to show: " + adError.getMessage());
                                    adClosed = true;
                                    mInterstitialAd = null;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(Exception error) {
                            Log.e(TAG, "Interstitial Ad Failed to Load: " + error.getMessage());
                            mInterstitialAd = null;
                        }
                    });
        });
    }

    public void showAd() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(mActivity);
            } else {
                Log.w(TAG, "The interstitial ad wasn't loaded yet.");
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