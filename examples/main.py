import os
from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.clock import mainthread, Clock

from pygma import (
    GMAManager,
    GMABannerAd,
    GMAInterstitialAd,
    GMARewardedAd,
    GMARewardedInterstitialAd,
    GMAAppOpenAd,
)

# Google Official Test IDs (Safe for development)
TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917"
TEST_REWARDED_INT_ID = "ca-app-pub-3940256099942544/5354046379"
TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"


class MainUI(BoxLayout):
    def __init__(self, **kwargs):
        super().__init__(orientation="vertical", padding=20, spacing=10, **kwargs)

        self.coins = 0

        self.status_label = Label(
            text="Initializing SDK...",
            font_size="16sp",
            size_hint_y=0.2,
            halign="center",
        )
        self.add_widget(self.status_label)

        self.stats_label = Label(
            text="Coins: 0 | Impressions: 0 | Clicks: 0",
            font_size="18sp",
            size_hint_y=0.1,
            color=(1, 0.8, 0, 1),
        )
        self.add_widget(self.stats_label)

        self.btn_interstitial = Button(text="Show Interstitial", disabled=True)
        self.add_widget(self.btn_interstitial)

        self.btn_rewarded = Button(text="Watch Video for Coins", disabled=True)
        self.add_widget(self.btn_rewarded)

        self.btn_rewarded_int = Button(
            text="Next Level (Rewarded Interstitial)", disabled=True
        )
        self.add_widget(self.btn_rewarded_int)

        self.btn_app_open = Button(text="Test App Open Ad manually", disabled=True)
        self.add_widget(self.btn_app_open)

        banner_controls = BoxLayout(orientation="horizontal", spacing=10)
        self.btn_show_banner = Button(text="Show Banner", disabled=True)
        self.btn_hide_banner = Button(text="Hide Banner", disabled=True)
        banner_controls.add_widget(self.btn_show_banner)
        banner_controls.add_widget(self.btn_hide_banner)
        self.add_widget(banner_controls)


class MyAdGameApp(App):
    def build(self):
        self.ui = MainUI()
        self.impressions = 0
        self.clicks = 0

        self.ui.btn_interstitial.bind(on_press=self.show_interstitial)
        self.ui.btn_rewarded.bind(on_press=self.show_rewarded)
        self.ui.btn_rewarded_int.bind(on_press=self.show_rewarded_int)
        self.ui.btn_app_open.bind(on_press=self.show_app_open)

        self.ui.btn_show_banner.bind(on_press=self.show_banner)
        self.ui.btn_hide_banner.bind(on_press=self.hide_banner)

        return self.ui

    def on_start(self):
        """Phase 1: Boot the SDK natively on a background thread"""
        GMAManager.initialize(TEST_APP_ID)
        Clock.schedule_interval(self.check_sdk_ready, 0.1)

    def check_sdk_ready(self, dt):
        """Phase 2: Wait for background init to finish, then load ads"""
        if GMAManager.is_initialized():
            self.ui.status_label.text = "SDK Ready. Caching all ads..."
            self.setup_ads()
            Clock.unschedule(self.check_sdk_ready)
            return False
        return True

    def setup_ads(self):
        """Phase 3: Instantiate all formats mapping to the unified listener"""

        # Banner Ad
        self.banner_ad = GMABannerAd(
            TEST_BANNER_ID,
            on_ad_loaded=lambda: self.on_ad_loaded("Banner"),
            on_ad_failed=self.on_ad_failed,
            on_ad_clicked=self.on_ad_clicked,
            on_ad_impression=self.on_ad_impression,
        )

        # App Open Ad (Automatically preloads itself)
        self.app_open_ad = GMAAppOpenAd(
            TEST_APP_OPEN_ID,
            on_ad_loaded=lambda: self.on_ad_loaded("App Open"),
            on_ad_closed=self.on_ad_closed,
            on_ad_failed=self.on_ad_failed,
            on_ad_clicked=self.on_ad_clicked,
            on_ad_impression=self.on_ad_impression,
        )

        # Interstitial Ad
        self.interstitial_ad = GMAInterstitialAd(
            TEST_INTERSTITIAL_ID,
            on_ad_loaded=lambda: self.on_ad_loaded("Interstitial"),
            on_ad_closed=self.on_ad_closed,
            on_ad_failed=self.on_ad_failed,
            on_ad_clicked=self.on_ad_clicked,
            on_ad_impression=self.on_ad_impression,
        )

        # Rewarded Ad
        self.rewarded_ad = GMARewardedAd(
            TEST_REWARDED_ID,
            on_ad_loaded=lambda: self.on_ad_loaded("Rewarded"),
            on_reward_earned=self.on_reward_earned,
            on_ad_closed=self.on_ad_closed,
            on_ad_failed=self.on_ad_failed,
            on_ad_clicked=self.on_ad_clicked,
            on_ad_impression=self.on_ad_impression,
        )

        # Rewarded Interstitial Ad
        self.rewarded_int_ad = GMARewardedInterstitialAd(
            TEST_REWARDED_INT_ID,
            on_ad_loaded=lambda: self.on_ad_loaded("Rewarded Interstitial"),
            on_reward_earned=self.on_reward_earned,
            on_ad_closed=self.on_ad_closed,
            on_ad_failed=self.on_ad_failed,
            on_ad_clicked=self.on_ad_clicked,
            on_ad_impression=self.on_ad_impression,
        )

        # Pre-load the manually triggered fullscreen ads
        self.interstitial_ad.load()
        self.rewarded_ad.load()
        self.rewarded_int_ad.load()

        self.ui.btn_interstitial.disabled = False
        self.ui.btn_rewarded.disabled = False
        self.ui.btn_rewarded_int.disabled = False
        self.ui.btn_app_open.disabled = False
        self.ui.btn_show_banner.disabled = False
        self.ui.btn_hide_banner.disabled = False

    def on_resume(self):
        """Kivy Lifecycle Hook: Triggered when app returns from background."""
        if hasattr(self, "app_open_ad"):
            self.ui.status_label.text = "App resumed. Showing App Open Ad..."
            self.app_open_ad.show()

    # ==========================================
    # DISPLAY TRIGGERS
    # ==========================================

    def show_interstitial(self, instance):
        if self.interstitial_ad.is_loaded():
            self.interstitial_ad.show()
        else:
            self.ui.status_label.text = "Interstitial not ready. Fetching..."
            self.interstitial_ad.load()

    def show_rewarded(self, instance):
        if self.rewarded_ad.is_loaded():
            self.rewarded_ad.show()
        else:
            self.ui.status_label.text = "Rewarded Ad not ready. Fetching..."
            self.rewarded_ad.load()

    def show_rewarded_int(self, instance):
        if self.rewarded_int_ad.is_loaded():
            self.rewarded_int_ad.show()
        else:
            self.ui.status_label.text = "Rewarded Interstitial not ready. Fetching..."
            self.rewarded_int_ad.load()

    def show_app_open(self, instance):
        # We don't check is_loaded() here because the wrapper handles it securely
        self.app_open_ad.show()

    def show_banner(self, instance):
        self.banner_ad.show()

    def hide_banner(self, instance):
        self.banner_ad.hide()

    # ==========================================
    # CENTRALIZED AD LISTENER INTERFACE
    # ==========================================

    @mainthread
    def on_ad_loaded(self, format_name):
        self.ui.status_label.text = f"[{format_name}] Ad Successfully Loaded!"

    @mainthread
    def on_ad_closed(self):
        """Triggered universally when ANY fullscreen ad is dismissed."""
        self.ui.status_label.text = f"Ad Closed. Resuming game..."

        # Pre-cache the next round of ads automatically
        self.interstitial_ad.load()
        self.rewarded_ad.load()
        self.rewarded_int_ad.load()

    @mainthread
    def on_reward_earned(self):
        """Triggered universally when ANY rewarded format qualifies for a payout."""
        self.ui.coins += 100
        self.update_stats()
        self.ui.status_label.text = "🎉 Reward Earned! +100 Coins"

    @mainthread
    def on_ad_failed(self, error_msg):
        """Triggered universally when an ad fails to fetch or render."""
        self.ui.status_label.text = f"Network/Ad Error: {error_msg}"

    @mainthread
    def on_ad_clicked(self):
        """Analytics: User physically tapped an ad."""
        self.clicks += 1
        self.update_stats()
        print("[Analytics] AD CLICKED!")

    @mainthread
    def on_ad_impression(self):
        """Analytics: Ad successfully rendered on the user's screen."""
        self.impressions += 1
        self.update_stats()
        print("[Analytics] AD IMPRESSION RECORDED!")

    def update_stats(self):
        self.ui.stats_label.text = f"Coins: {self.ui.coins} | Impressions: {self.impressions} | Clicks: {self.clicks}"


def main():
    MyAdGameApp().run()


if __name__ == "__main__":
    main()
