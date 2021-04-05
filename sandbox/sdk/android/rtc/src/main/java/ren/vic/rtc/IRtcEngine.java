package ren.vic.rtc;

import android.content.Context;

import ren.vic.rtc.internal.RtcEngineImpl;

public abstract class IRtcEngine {
    private static RtcEngineImpl mInstance = null;

    public static synchronized IRtcEngine create(Context context) throws Exception {
        if (mInstance == null) {
            mInstance = new RtcEngineImpl();
        } else {
        }
        return mInstance;
    }
}
