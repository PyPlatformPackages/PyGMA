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
    private PyGMAListener mListener;
    private boolean isShowingAd = false;

    public PyAppOpenAd(Activity activity, String adUnitId, PyGMAListener listener) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
        this.mListener = listener;
    }

    public void loadAd() {
        if (isShowingAd || mAppOpenAd != null)
            return;

        new Handler(Looper.getMainLooper()).post(() -> {
            AdRequest request = new AdRequest.Builder(mAdUnitId).build();

            AppOpenAd.load(request, new AdLoadCallback<AppOpenAd>() {
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    mAppOpenAd = ad;
                    if (mListener != null)
                        mListener.onAdLoaded();

                    mAppOpenAd.setAdEventCallback(new AppOpenAdEventCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            if (mListener != null)
                                mListener.onAdClosed();
                            mAppOpenAd = null;
                            isShowingAd = false;
                            loadAd(); // Auto-preload next
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(FullScreenContentError error) {
                            if (mListener != null)
                                mListener.onAdFailed(error.getMessage());
                            isShowingAd = false;
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
}