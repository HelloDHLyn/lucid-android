package com.lynlab.lucid.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author lyn
 * @since 2016/09/07
 */
public class ApplicationUtil {

    /**
     * 패키지 이름으로 앱이 설치되어있는지 확인한다.
     */
    public static boolean isApplicationInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isApplicationLatest(Context context, String packageName, String versionName, int versionCode)
            throws PackageManager.NameNotFoundException {
        PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
        return versionCode == info.versionCode && versionName.equals(info.versionName);

    }
}
