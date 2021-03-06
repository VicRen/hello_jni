package ren.vic.rtc;

import android.content.Context;
import org.webrtc.ContextUtils;
import ren.vic.rtc.internal.RtcEngineImpl;

public abstract class RtcEngine {
    private static RtcEngineImpl mInstance = null;

    public RtcEngine() {
    }

    public static synchronized RtcEngine create(Context context, String appId, IRtcEngineEventHandler handler) throws Exception {
        if (context != null) {
            ContextUtils.initialize(context);
            if (mInstance == null) {
                mInstance = new RtcEngineImpl(context, appId, handler);
            }
            return mInstance;
        } else {
            return null;
        }
    }

    public abstract int TestingInt();

    public abstract int joinChannel(String token, String channelName, String optionalInfo, int optionalUid);
}
