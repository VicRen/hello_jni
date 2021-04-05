package ren.vic.rtc.internal;

import ren.vic.rtc.IRtcEngine;

public class RtcEngineImpl extends IRtcEngine {

    public static synchronized void loadNativeLibrary() {
        System.loadLibrary("rtc-sdk");
    }

    public static synchronized boolean initializeNativeLibs() {
        return false;
    }
}
