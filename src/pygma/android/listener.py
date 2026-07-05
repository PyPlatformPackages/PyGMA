from jnius import PythonJavaClass, java_method

class NativeAdListener(PythonJavaClass):
    __javainterfaces__ = ['PyGMA/PyGMAListener']
    __javacontext__ = 'app'

    on_loaded = None
    on_closed = None
    on_failed = None
    on_rewarded = None
    on_clicked = None
    on_impression = None

    @java_method('()V')
    def onAdLoaded(self):
        if self.on_loaded: 
            self.on_loaded()

    @java_method('()V')
    def onAdClosed(self):
        if self.on_closed: 
            self.on_closed()

    @java_method('(Ljava/lang/String;)V')
    def onAdFailed(self, error: str):
        if self.on_failed: 
            self.on_failed(error)

    @java_method('()V')
    def onRewardEarned(self):
        if self.on_rewarded: 
            self.on_rewarded()

    @java_method('()V')
    def onAdClicked(self):
        if self.on_clicked: 
            self.on_clicked()

    @java_method('()V')
    def onAdImpression(self):
        if self.on_impression: 
            self.on_impression()