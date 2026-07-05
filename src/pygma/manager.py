from .platform_detection import get_current_platform


class GMAManager:
    @classmethod
    def initialize(cls, app_id: str):
        platform = get_current_platform()
        if platform == "android":
            from .android.manager import AndroidGMAManager

            AndroidGMAManager.initialize(app_id)
        elif platform == "ios":
            # from .ios.manager import IOSGMAManager
            # IOSGMAManager.initialize(app_id)
            pass

    @classmethod
    def is_initialized(cls) -> bool:
        platform = get_current_platform()
        if platform == "android":
            from .android.manager import AndroidGMAManager

            return AndroidGMAManager.is_initialized()
        return False
