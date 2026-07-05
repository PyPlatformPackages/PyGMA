import os
from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.clock import mainthread

from pygma import GMAManager, GMABannerAd, GMAInterstitialAd, GMARewardedAd

TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917"


class MainUI(BoxLayout):
    def __init__(self, **kwargs):
        super().__init__(orientation="vertical", padding=20, spacing=15, **kwargs)

        self.coins = 0

        self.status_label = Label(
            text="Initializing Ads...", font_size="20sp", size_hint_y=0.2
        )
        self.add_widget(self.status_label)

        self.btn_interstitial = Button(
            text="Show Interstitial (Loading...)", disabled=True
        )
        self.btn_interstitial.bind(on_press=self.show_interstitial)
        self.add_widget(self.btn_interstitial)

        self.btn_rewarded = Button(
            text="Watch Video for Coins (Loading...)", disabled=True
        )
        self.btn_rewarded.bind(on_press=self.show_rewarded)
        self.add_widget(self.btn_rewarded)

        self.btn_show_banner = Button(text="Show Bottom Banner")
        self.btn_show_banner.bind(on_press=self.show_banner)
        self.add_widget(self.btn_show_banner)

        self.btn_hide_banner = Button(text="Hide Banner")
        self.btn_hide_banner.bind(on_press=self.hide_banner)
        self.add_widget(self.btn_hide_banner)


class MyAdGameApp(App):
    def build(self):
        self.ui = MainUI()
        return self.ui

    def on_start(self):
        """Called immediately after the Kivy UI is fully loaded and drawn."""
        GMAManager.initialize(TEST_APP_ID)
        self.ui.status_label.text = "SDK Initialized. Loading ads..."

        self.banner_ad = GMABannerAd(TEST_BANNER_ID)

        self.interstitial_ad = GMAInterstitialAd(
            ad_unit_id=TEST_INTERSTITIAL_ID,
            on_ad_closed=self.on_interstitial_closed,
            on_ad_failed=self.on_ad_failed,
        )

        self.rewarded_ad = GMARewardedAd(
            ad_unit_id=TEST_REWARDED_ID,
            on_reward_earned=self.on_reward_earned,
            on_ad_closed=self.on_rewarded_closed,
            on_ad_failed=self.on_ad_failed,
        )

        self.interstitial_ad.load()
        self.rewarded_ad.load()

        self.ui.btn_interstitial.disabled = False
        self.ui.btn_interstitial.text = "Show Interstitial"
        self.ui.btn_rewarded.disabled = False
        self.ui.btn_rewarded.text = "Watch Video for Coins"

    def show_interstitial(self, instance):
        if self.interstitial_ad.is_loaded():
            self.interstitial_ad.show()
        else:
            self.ui.status_label.text = "Interstitial not ready yet."

    def show_rewarded(self, instance):
        if self.rewarded_ad.is_loaded():
            self.rewarded_ad.show()
        else:
            self.ui.status_label.text = "Rewarded Ad not ready yet."

    def show_banner(self, instance):
        self.banner_ad.show()

    def hide_banner(self, instance):
        self.banner_ad.hide()

    @mainthread
    def on_interstitial_closed(self):
        self.ui.status_label.text = "Interstitial Closed. Reloading..."
        self.interstitial_ad.load()

    @mainthread
    def on_rewarded_closed(self):
        self.ui.status_label.text = f"Video Closed. Total Coins: {self.ui.coins}"
        self.rewarded_ad.load()

    @mainthread
    def on_reward_earned(self):
        self.ui.coins += 100
        self.ui.status_label.text = "You earned 100 coins!"

    @mainthread
    def on_ad_failed(self, error_msg):
        self.ui.status_label.text = f"Ad Error: {error_msg}"


if __name__ == "__main__":
    MyAdGameApp().run()
