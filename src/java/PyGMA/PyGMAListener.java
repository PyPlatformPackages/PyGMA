package PyGMA;

public interface PyGMAListener {
    void onAdLoaded();
    void onAdClosed();
    void onAdFailed(String error);
    void onRewardEarned();
    void onAdClicked();
    void onAdImpression();
}