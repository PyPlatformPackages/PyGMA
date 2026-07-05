from .platform_detection import get_current_platform


class GMAAppOpenAd:
    def __init__(self, ad_unit_id: str, on_ad_closed=None):
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.app_open import AndroidGMAAppOpenAd

            self._impl = AndroidGMAAppOpenAd(ad_unit_id, on_ad_closed)

    def show(self):
        if self._impl:
            self._impl.show()
