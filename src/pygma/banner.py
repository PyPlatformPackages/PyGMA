from .platform_detection import get_current_platform


class GMABannerAd:
    def __init__(
        self,
        ad_unit_id: str,
        on_ad_loaded=None,
        on_ad_failed=None,
        on_ad_clicked=None,
        on_ad_impression=None,
        on_ad_closed=None,
    ):
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.banner import AndroidGMABannerAd

            self._impl = AndroidGMABannerAd(
                ad_unit_id,
                on_ad_loaded,
                on_ad_failed,
                on_ad_clicked,
                on_ad_impression,
                on_ad_closed,
            )

    def show(self):
        if self._impl:
            self._impl.show()

    def hide(self):
        if self._impl:
            self._impl.hide()
