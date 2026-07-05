package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd;
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd.AppOpenAdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.adrequest.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.FullScreenContentCallback;
import com.google.android.libraries.ads.mobile.sdk.common.AdError;

public class PyAppOpenAd {
    private static final String TAG = "PyAppOpenAd";
    private AppOpenAd mAppOpenAd;
    private Activity mActivity;
    private String mAdUnitId;

    private boolean isShowingAd = false;
    private boolean adClosed = false;

    public PyAppOpenAd(Activity activity, String adUnitId) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
    }

    public void loadAd() {
        if (isShowingAd || mAppOpenAd != null)
            return;

        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder().build();

            AppOpenAd.load(
                    mActivity, mAdUnitId, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            mAppOpenAd = ad;
                            adClosed = false;
                        }

                        @Override
                        public void onAdFailedToLoad(Exception error) {
                            mAppOpenAd = null;
                        }
                    });
        });
    }

    public void showAdIfAvailable() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (!isShowingAd && mAppOpenAd != null) {
                mAppOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mAppOpenAd = null;
                        isShowingAd = false;
                        adClosed = true;
                        loadAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        isShowingAd = false;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        isShowingAd = true;
                    }
                });
                mAppOpenAd.show(mActivity);
            } else {
                loadAd();
            }
        });
    }

    public boolean checkAndResetAdClosed() {
        if (adClosed) {
            adClosed = false;
            return true;
        }
        return false;
    }
}