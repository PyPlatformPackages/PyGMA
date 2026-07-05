import sys
import os


def get_current_platform() -> str:
    """Returns 'android', 'ios', or the standard sys.platform name."""
    if sys.platform == "android" or "ANDROID_ROOT" in os.environ or "ANDROID_ARGUMENT" in os.environ:
        return "android"
    if sys.platform == "ios" or os.environ.get("HOME", "").startswith("/var/mobile"):
        return "ios"
    return sys.platform
