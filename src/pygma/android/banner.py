from .bridge import AndroidBridge
from .listener import NativeAdListener


class AndroidGMABannerAd:
    def __init__(
        self,
        ad_unit_id: str,
        on_ad_loaded=None,
        on_ad_failed=None,
        on_ad_clicked=None,
        on_ad_impression=None,
        on_ad_closed=None,
    ):
        AndroidBridge.load_classes()

        self.listener = NativeAdListener(
            on_loaded=on_ad_loaded,
            on_failed=on_ad_failed,
            on_clicked=on_ad_clicked,
            on_impression=on_ad_impression,
            on_closed=on_ad_closed,
        )

        self._j_ad = AndroidBridge.PyBannerAd(
            AndroidBridge.activity, ad_unit_id, self.listener
        )

    def show(self):
        self._j_ad.showBanner()

    def hide(self):
        self._j_ad.hideBanner()
