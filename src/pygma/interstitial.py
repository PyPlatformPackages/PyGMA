from .platform_detection import get_current_platform


class GMAInterstitialAd:
    def __init__(
        self,
        ad_unit_id: str,
        on_ad_loaded=None,
        on_ad_closed=None,
        on_ad_failed=None,
        on_ad_clicked=None,
        on_ad_impression=None,
    ):
        """
        Public cross-platform facing class for Interstitial Ads.
        """
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.interstitial import AndroidGMAInterstitialAd

            self._impl = AndroidGMAInterstitialAd(
                ad_unit_id,
                on_ad_loaded,
                on_ad_closed,
                on_ad_failed,
                on_ad_clicked,
                on_ad_impression,
            )
        elif self.platform == "ios":
            # Future hooks for iOS implementation go here:
            # from .ios.interstitial import IOSGMAInterstitialAd
            # self._impl = IOSGMAInterstitialAd(ad_unit_id, on_ad_closed, on_ad_failed)
            pass

    def load(self):
        """Requests a new full-screen interstitial asset from the active engine."""
        if self._impl:
            self._impl.load()

    def is_loaded(self) -> bool:
        """Checks if an interstitial layout is cached and ready to display."""
        return self._impl.is_loaded() if self._impl else False

    def show(self):
        """Renders the interstitial ad on top of the native OS window layout."""
        if self._impl:
            self._impl.show()
