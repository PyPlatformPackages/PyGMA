import threading
import time
from .bridge import AndroidBridge


class AndroidGMAAppOpenAd:
    def __init__(self, ad_unit_id: str, on_ad_closed=None):
        AndroidBridge.load_classes()
        self._j_ad = AndroidBridge.PyAppOpenAd(AndroidBridge.activity, ad_unit_id)
        self.on_ad_closed = on_ad_closed
        self._keep_monitoring = False

        self._j_ad.loadAd()

    def show(self):
        self._j_ad.showAdIfAvailable()
        self._keep_monitoring = True
        threading.Thread(target=self._poll_ad_events, daemon=True).start()

    def _poll_ad_events(self):
        while self._keep_monitoring:
            if self._j_ad.checkAndResetAdClosed():
                self._keep_monitoring = False
                if self.on_ad_closed:
                    self.on_ad_closed()
                break
            time.sleep(0.05)
