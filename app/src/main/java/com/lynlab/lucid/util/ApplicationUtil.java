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

    /**
     * 설치되어있는 패키지가 특정 버전인지 확인한다.
     *
     * @param packageName 확인 할 패키지 이름
     * @param versionName 해당 패키지의 버전명
     * @param versionCode 해당 패키지의 버전 코드
     * @return 패키지가 해당 버전일 경우 true, 아닐 경우 false
     * @throws PackageManager.NameNotFoundException
     */
    public static boolean checkInstalledVersion(Context context, String packageName, String versionName, int versionCode)
            throws PackageManager.NameNotFoundException {
        PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
        return versionCode == info.versionCode && versionName.equals(info.versionName);
    }
}
