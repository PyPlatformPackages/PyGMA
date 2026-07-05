from .bridge import AndroidBridge


class AndroidGMABannerAd:
    def __init__(self, ad_unit_id: str):
        AndroidBridge.load_classes()
        self._j_ad = AndroidBridge.PyBannerAd(AndroidBridge.activity, ad_unit_id)

    def show(self):
        self._j_ad.showBanner()

    def hide(self):
        self._j_ad.hideBanner()
