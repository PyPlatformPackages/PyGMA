from .platform_detection import get_current_platform


class GMAAppOpenAd:
    def __init__(self, 
        ad_unit_id: str,
        on_ad_loaded=None,
        on_ad_closed=None,
        on_ad_failed=None,
        on_ad_clicked=None,
        on_ad_impression=None,
    ):
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.app_open import AndroidGMAAppOpenAd

            self._impl = AndroidGMAAppOpenAd(ad_unit_id, on_ad_loaded, on_ad_closed, on_ad_failed, on_ad_clicked, on_ad_impression)

    def show(self):
        if self._impl:
            self._impl.show()
