from jnius import PythonJavaClass, java_method

class NativeAdListener(PythonJavaClass):
    __javainterfaces__ = ['PyGMA/PyGMAListener']
    __javacontext__ = 'app'

    def __init__(self, on_loaded=None, on_closed=None, on_failed=None, 
                 on_rewarded=None, on_clicked=None, on_impression=None):
        super().__init__()
        self._on_loaded = on_loaded
        self._on_closed = on_closed
        self._on_failed = on_failed
        self._on_rewarded = on_rewarded
        self._on_clicked = on_clicked
        self._on_impression = on_impression

    @java_method('()V')
    def onAdLoaded(self):
        if self._on_loaded: 
            self._on_loaded()

    @java_method('()V')
    def onAdClosed(self):
        if self._on_closed: 
            self._on_closed()

    @java_method('(Ljava/lang/String;)V')
    def onAdFailed(self, error: str):
        if self._on_failed: 
            self._on_failed(error)

    @java_method('()V')
    def onRewardEarned(self):
        if self._on_rewarded: 
            self._on_rewarded()

    @java_method('()V')
    def onAdClicked(self):
        if self._on_clicked: 
            self._on_clicked()

    @java_method('()V')
    def onAdImpression(self):
        if self._on_impression: 
            self._on_impression()