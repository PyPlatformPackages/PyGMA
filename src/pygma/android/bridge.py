import os


class AndroidBridge:

    _initialized = False

    activity = None
    PyGMAManager = None
    PyBannerAd = None
    PyInterstitialAd = None
    PyRewardedAd = None
    PyRewardedInterstitialAd = None
    PyAppOpenAd = None

    @classmethod
    def load_classes(cls):
        """Resolves all required Java classes at once."""
        if cls._initialized:
            return

        from jnius import autoclass

        activity_env = os.environ.get("APP_ACTIVITY", "org.kivy.android.PythonActivity")
        activity_class = autoclass(activity_env)
        cls.activity = (
            activity_class.mActivity
            if hasattr(activity_class, "mActivity")
            else activity_class
        )

        cls.PyGMAManager = autoclass("PyGMA.PyGMAManager")
        cls.PyBannerAd = autoclass("PyGMA.PyBannerAd")
        cls.PyInterstitialAd = autoclass("PyGMA.PyInterstitialAd")
        cls.PyRewardedAd = autoclass("PyGMA.PyRewardedAd")
        cls.PyRewardedInterstitialAd = autoclass(
            "PyGMA.PyRewardedInterstitialAd"
        )
        cls.PyAppOpenAd = autoclass("PyGMA.PyAppOpenAd")

        cls._initialized = True
