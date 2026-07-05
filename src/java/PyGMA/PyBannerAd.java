package PyGMA;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.util.Log;

import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest;
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize;
import com.google.android.libraries.ads.mobile.sdk.banner.AdView;
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;

public class PyBannerAd {
    private static final String TAG = "PyBannerAd";
    private Activity mActivity;
    private String mAdUnitId;
    private AdView mAdView;
    private LinearLayout bannerLayout;

    public PyBannerAd(Activity activity, String adUnitId) {
        this.mActivity = activity;
        this.mAdUnitId = adUnitId;
    }

    public void showBanner() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (bannerLayout == null) {
                bannerLayout = new LinearLayout(mActivity);
                bannerLayout.setOrientation(LinearLayout.VERTICAL);
                bannerLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                mActivity.addContentView(
                        bannerLayout,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }

            if (mAdView == null) {
                mAdView = new AdView(mActivity);
                bannerLayout.addView(mAdView);
            }

            AdSize adSize = AdSize.getLargeAnchoredAdaptiveBannerAdSize(mActivity, 360);
            BannerAdRequest request = new BannerAdRequest.Builder(mAdUnitId, adSize).build();

            mAdView.loadAd(request, new AdLoadCallback<BannerAd>() {
                @Override
                public void onAdLoaded(BannerAd ad) {
                    Log.d(TAG, "Banner Loaded");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError error) {
                    Log.e(TAG, "Banner Failed: " + error.getMessage());
                }
            });
        });
    }

    public void hideBanner() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (bannerLayout != null) {
                bannerLayout.removeAllViews();
                mAdView = null;
            }
        });
    }
}