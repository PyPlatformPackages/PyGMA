import threading
import time
from .bridge import AndroidBridge


class AndroidGMAInterstitialAd:
    def __init__(self, ad_unit_id: str, on_ad_closed=None, on_ad_failed=None):
        AndroidBridge.load_classes()

        self._j_ad = AndroidBridge.PyInterstitialAd(AndroidBridge.activity, ad_unit_id)

        self.on_ad_closed = on_ad_closed
        self.on_ad_failed = on_ad_failed
        self._keep_monitoring = False

    def load(self):
        self._j_ad.loadAd()

    def is_loaded(self) -> bool:
        return self._j_ad.isLoaded()

    def show(self):
        if self.is_loaded():
            self._j_ad.showAd()
            self._keep_monitoring = True
            threading.Thread(target=self._poll_ad_events, daemon=True).start()
        elif self.on_ad_failed:
            self.on_ad_failed("Ad not loaded yet.")

    def _poll_ad_events(self):
        while self._keep_monitoring:
            if self._j_ad.checkAndResetAdClosed():
                self._keep_monitoring = False
                if self.on_ad_closed:
                    self.on_ad_closed()
                break
            time.sleep(0.05)
