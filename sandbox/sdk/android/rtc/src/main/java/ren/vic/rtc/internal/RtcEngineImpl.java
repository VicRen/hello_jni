package ren.vic.rtc.internal;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import ren.vic.rtc.IRtcEngineEventHandler;
import ren.vic.rtc.RtcEngine;
import ren.vic.rtc.models.Events;

public class RtcEngineImpl extends RtcEngine {

    private long mNativeHandle = 0L;
    private WeakReference<Context> mContext;
    private final ConcurrentHashMap<IRtcEngineEventHandler, Integer> mRtcHandlers = new ConcurrentHashMap<>();

    public RtcEngineImpl(Context context, String appId, IRtcEngineEventHandler handler) throws Exception {
        mContext = new WeakReference<>(context);
        mNativeHandle = nativeObjectInit(context);
        addHandler(handler);
    }

    public void reinitialize(Context context, String appId, IRtcEngineEventHandler handler) {
        this.addHandler(handler);
    }

    public void addHandler(IRtcEngineEventHandler handler) {
        this.mRtcHandlers.put(handler, 0);
    }

    public void removeHandler(IRtcEngineEventHandler handler) {
        this.mRtcHandlers.remove(handler);
    }

    @Override
    public int TestingInt() {
        return nativeTestingInt();
    }

    @Override
    public int joinChannel(String token, String channelName, String optionalInfo, int optionalUid) {
        Context context = mContext.get();
        if (context == null) {
            return -7;
        }
        return 0;
    }

    protected void onEvent(String name, String info) {
        Log.i("----------->RtcEngine", String.format("onEvent: %s, %s", name, info));
        try {
            for (IRtcEngineEventHandler h : this.mRtcHandlers.keySet()) {
                h.onError(0);
            }
        } catch (Exception e) {
            Log.e("RtcEngine", "onEvent: " + e.toString());
        }
    }

    protected void onEvent(int eventId, byte[] evt) {
        Log.i("----------->RtcEngine", "onEvent: ");
        try {
            Iterator<IRtcEngineEventHandler> it = this.mRtcHandlers.keySet().iterator();

            while(it.hasNext()) {
                IRtcEngineEventHandler h = it.next();
                if (h == null) {
                    it.remove();
                } else {
                    this.handleEvent(eventId, evt, h);
                }
            }
        } catch (Exception e) {
            Log.e("RtcEngine", "onEvent: " + e.toString());
        }
    }

    protected void handleEvent(int eventId, byte[] evt, IRtcEngineEventHandler handler) {
        Log.d("=========event", eventId + " bytelen=" + evt.length + " byte:" + Arrays.toString(evt));
        if (handler != null) {
            RtcEngineMessage.PError pe;
            switch (eventId) {
                case Events.ERROR:
//                    pe = new RtcEngineMessage.PError();
//                    pe.unmarshall(evt);
//                    Log.i("=========", "Events.ERROR err:" + pe.err);
                    handler.onError(0);
                    break;
            }
        }
    }

    private native long nativeObjectInit(Context context);

    private native int nativeTestingInt();

    private native int nativeJoinChannel(long nativeHandle, byte[] appContext, String token, String channelName, String info, int uid);
}
