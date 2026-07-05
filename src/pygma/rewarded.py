from .platform_detection import get_current_platform


class GMARewardedAd:
    def __init__(
        self,
        ad_unit_id: str,
        on_reward_earned=None,
        on_ad_closed=None,
        on_ad_failed=None,
    ):
        self.platform = get_current_platform()
        self._impl = None

        if self.platform == "android":
            from .android.rewarded import AndroidGMARewardedAd

            self._impl = AndroidGMARewardedAd(
                ad_unit_id, on_reward_earned, on_ad_closed, on_ad_failed
            )

    def load(self):
        if self._impl:
            self._impl.load()

    def is_loaded(self) -> bool:
        return self._impl.is_loaded() if self._impl else False

    def show(self):
        if self._impl:
            self._impl.show()
