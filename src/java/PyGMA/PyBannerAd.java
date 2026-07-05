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
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdLoadCallback;

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
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }

            if (mAdView == null) {
                mAdView = new AdView(mActivity);
                bannerLayout.addView(mAdView);
            }

            BannerAdRequest request = new BannerAdRequest.Builder(AdSize.BANNER, mAdUnitId).build();

            BannerAd.load(
                    mActivity,
                    request,
                    new BannerAdLoadCallback() {
                        @Override
                        public void onAdLoaded(BannerAd bannerAd) {
                            Log.d(TAG, "Banner Loaded");
                            mAdView.registerBannerAd(bannerAd);
                        }

                        @Override
                        public void onAdFailedToLoad(Exception error) {
                            Log.e(TAG, "Banner Failed to load: " + error.getMessage());
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