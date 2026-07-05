from .bridge import AndroidBridge
from .listener import NativeAdListener


class AndroidGMAAppOpenAd:
    def __init__(
        self,
        ad_unit_id: str,
        on_ad_loaded=None,
        on_ad_closed=None,
        on_ad_failed=None,
        on_ad_clicked=None,
        on_ad_impression=None,
    ):
        AndroidBridge.load_classes()

        self.listener = NativeAdListener()
        self.listener.on_loaded = on_ad_loaded
        self.listener.on_closed = on_ad_closed
        self.listener.on_failed = on_ad_failed
        self.listener.on_clicked = on_ad_clicked
        self.listener.on_impression = on_ad_impression

        self._j_ad = AndroidBridge.PyAppOpenAd(
            AndroidBridge.activity, ad_unit_id, self.listener
        )
        self._j_ad.loadAd()

    def show(self):
        self._j_ad.showAdIfAvailable()
