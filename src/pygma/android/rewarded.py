from .bridge import AndroidBridge
from .listener import NativeAdListener


class AndroidGMARewardedAd:
    def __init__(
        self,
        ad_unit_id: str,
        on_ad_loaded=None,
        on_reward_earned=None,
        on_ad_closed=None,
        on_ad_failed=None,
        on_ad_clicked=None,
        on_ad_impression=None,
    ):
        AndroidBridge.load_classes()

        self.listener = NativeAdListener()
        self.listener.on_loaded = on_ad_loaded
        self.listener.on_rewarded = on_reward_earned
        self.listener.on_closed = on_ad_closed
        self.listener.on_failed = on_ad_failed
        self.listener.on_clicked = on_ad_clicked
        self.listener.on_impression = on_ad_impression

        self._j_ad = AndroidBridge.PyRewardedAd(
            AndroidBridge.activity, ad_unit_id, self.listener
        )

    def load(self):
        self._j_ad.loadAd()

    def is_loaded(self) -> bool:
        return self._j_ad.isLoaded()

    def show(self):
        if self.is_loaded():
            self._j_ad.showAd()
