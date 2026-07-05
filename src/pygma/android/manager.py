from .bridge import AndroidBridge


class AndroidGMAManager:
    @classmethod
    def initialize(cls, app_id: str):
        AndroidBridge.load_classes()
        AndroidBridge.PyGMAManager.initialize(AndroidBridge.activity, app_id)

    @classmethod
    def is_initialized(cls) -> bool:
        if AndroidBridge._initialized:
            return AndroidBridge.PyGMAManager.isSdkInitialized()
        return False
