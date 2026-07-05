package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd;
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError;

public class PyAppOpenAd {
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
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            AppOpenAd.load(
                    request,
                    new AdLoadCallback<AppOpenAd>() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            mAppOpenAd = ad;
                            adClosed = false;

                            mAppOpenAd.setAdEventCallback(new AppOpenAdEventCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    mAppOpenAd = null;
                                    isShowingAd = false;
                                    adClosed = true;
                                    loadAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                                    isShowingAd = false;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError error) {
                            mAppOpenAd = null;
                        }
                    });
        });
    }

    public void showAdIfAvailable() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (!isShowingAd && mAppOpenAd != null) {
                isShowingAd = true;
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