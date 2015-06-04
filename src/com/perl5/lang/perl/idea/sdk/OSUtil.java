package com.perl5.lang.perl.idea.sdk;

import java.util.Locale;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public abstract class OSUtil {
    protected static OSType detectedOS;
    public enum OSType {
        Windows, MacOS, Linux, Other
    }

    static {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
    }

    public static boolean isWindows() {
        return detectedOS.equals(OSType.Windows);
    }

    public static boolean isMacOS() {
        return detectedOS.equals(OSType.MacOS);
    }

    public static boolean isLinux() {
        return detectedOS.equals(OSType.Linux);
    }

    public static boolean isKnown() {
        return !isOther();
    }
    public static boolean isOther() {
        return detectedOS.equals(OSType.Other);
    }
}
