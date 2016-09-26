package com.lynlab.lucid.model;

/**
 * 애플리케이션 설치 상태 enum
 * Created by lyn on 9/26/16.
 */

public enum InstallState {
    NOT_INSTALLED,      // 미설치
    NOT_LATEST,         // 설치되었으나 최신 버전 아님
    INSTALLED           // 최신 버전 설치됨
}
