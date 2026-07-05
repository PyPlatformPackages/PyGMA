from .platform_detection import get_current_platform


class GMABannerAd:
    def __init__(self, ad_unit_id: str):
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.banner import AndroidGMABannerAd

            self._impl = AndroidGMABannerAd(ad_unit_id)

    def show(self):
        if self._impl:
            self._impl.show()

    def hide(self):
        if self._impl:
            self._impl.hide()
