from .platform_detection import get_current_platform


class GMARewardedAd:
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
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.rewarded import AndroidGMARewardedAd

            self._impl = AndroidGMARewardedAd(
                ad_unit_id, on_ad_loaded, on_reward_earned, on_ad_closed, on_ad_failed, on_ad_clicked, on_ad_impression
            )

    def load(self):
        if self._impl:
            self._impl.load()

    def is_loaded(self) -> bool:
        return self._impl.is_loaded() if self._impl else False

    def show(self):
        if self._impl:
            self._impl.show()
