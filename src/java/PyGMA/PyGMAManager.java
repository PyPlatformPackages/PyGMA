package PyGMA;

import android.app.Activity;
import android.util.Log;
import com.google.android.libraries.ads.mobile.sdk.MobileAds;
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig;

public class PyGMAManager {
    private static final String TAG = "PyGMAManager";
    private static boolean isInitialized = false;

    public static void initialize(final Activity activity, final String appId) {
        if (isInitialized)
            return;

        new Thread(() -> {
            try {
                Log.d(TAG, "Initializing GMA Next-Gen SDK...");
                MobileAds.initialize(
                        activity,
                        new InitializationConfig.Builder(appId).build(),
                        initializationStatus -> {
                            Log.d(TAG, "GMA SDK Initialization Complete.");
                            isInitialized = true;
                        });
            } catch (Exception e) {
                Log.e(TAG, "Failed to initialize GMA SDK: " + e.getMessage());
            }
        }).start();
    }

    public static boolean isSdkInitialized() {
        return isInitialized;
    }
}