package ren.vic.helloaar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;

import com.lstaas.lsrtc.IRtcEngineEventHandler;
import com.lstaas.lsrtc.RtcEngine;
import com.lstaas.lsrtc.internal.RtcConstants;
import com.lstaas.lsrtc.internal.RtcEngineImpl;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.util.HashSet;
import java.util.Set;

public class EngineDelegate extends IRtcEngineEventHandler {

    public static EngineDelegate getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final EngineDelegate INSTANCE = new EngineDelegate();
    }

    private RtcEngine mEngine;
    private final Handler mMainHandler;
    private final Handler mWorkHandler;
    private final Set<Listener> mListeners;
    private final Set<Peer> mPeers;
    private Peer mPeer;
    private Stats mStats;
    private RtcEngine.VideoEncoderConfiguration mEncoderConfig;

    public EngineDelegate() {
        mListeners = new HashSet<>();
        mPeers = new HashSet<>();
        HandlerThread handlerThread = new HandlerThread("worker");
        handlerThread.start();
        mWorkHandler = new Handler(handlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
        mEncoderConfig = new RtcEngine.VideoEncoderConfiguration();
        mEncoderConfig.mBitrate = 2000000;
        mEncoderConfig.mMinBitrate = 300000;
        mEncoderConfig.mFrameRate = 15;
        mEncoderConfig.mVideoDimensions = new RtcEngine.VideoDimensions(640, 480);
    }

    public void initialize(Context context) {
        mWorkHandler.post(() -> {
            try {
                mEngine = RtcEngine.create(context, "1234", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    public void join(String channel) {
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onStateChanged(State.STATE_JOINING);
            }
        });
        mWorkHandler.post(() -> {
            mEngine.joinChannel("123", channel, "ls", "");
            mEngine.startVideo();
        });
    }

    public void leave() {
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onStateChanged(State.STATE_LEAVING);
            }
            mEngine.leaveChannel();
        });
    }

    public void toggleAudio(boolean isMute) {
        mWorkHandler.post(() -> mEngine.muteLocalAudioStream(isMute));
    }

    public void toggleVideo(boolean isMute) {
        mWorkHandler.post(() -> mEngine.muteLocalVideoStream(isMute));
    }

    public Set<Peer> getPeers() {
        return mPeers;
    }

    public Peer getLocalPeer() {
        return mPeer;
    }

    public Stats getStats() {
        if (mStats == null) {
            mStats = new Stats();
        }
        return mStats;
    }

    public void setupLocalVideo(SurfaceViewRenderer surfaceView) {
        mWorkHandler.post(() -> mEngine.setupLocalVideo(surfaceView));
        surfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        surfaceView.setVisibility(View.VISIBLE);
    }

    public void setupRemoteVideo(String uid, SurfaceViewRenderer surfaceView) {
        mWorkHandler.post(() -> mEngine.setupRemoteVideo(uid, surfaceView));
        surfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        surfaceView.setVisibility(View.VISIBLE);
    }

    public void setVideoEncoderConfiguration(RtcEngine.VideoEncoderConfiguration config) {
        mEncoderConfig = config;
        mWorkHandler.post(() -> mEngine.setVideoEncoderConfiguration(mEncoderConfig));
    }

    public RtcEngine.VideoEncoderConfiguration getVideoEncoderConfiguration() {
        return mEncoderConfig;
    }

    public void initSurfaceView(SurfaceViewRenderer surfaceView) {
        surfaceView.init(((RtcEngineImpl) mEngine).getEglBase().getEglBaseContext(), null);
    }

    @Override
    public void onJoinChannelSuccess(String channel, String uid, int elapsed) {
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onStateChanged(State.STATE_JOINED);
            }
        });
        mPeer = new Peer(uid);
    }

    @Override
    public void onLeaveChannel(RtcStats stats) {
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onStateChanged(State.STATE_INIT);
            }
        });
        mPeers.clear();
        mPeer = null;
        mStats = null;
    }

    @Override
    public void onUserJoined(String uid, int elapsed) {
        mPeers.add(new Peer(uid));
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onPeerChanged();
            }
        });
    }

    @Override
    public void onUserOffline(String uid, int reason) {
        for (Peer p : mPeers) {
            if (p.mUid.equals(uid)) {
                mPeers.remove(p);
            }
        }
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onPeerChanged();
            }
        });
    }

    @Override
    public void onRemoteAudioStateChanged(String uid, int state, int reason, int elapsed) {
        for (Peer p : mPeers) {
            if (p.mUid.equals(uid)) {
                p.mHasAudio = state == RtcConstants.REMOTE_AUDIO_STATE_STARTING
                        || state == RtcConstants.REMOTE_AUDIO_STATE_DECODING;
            }
        }
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onPeerChanged();
            }
        });
    }

    @Override
    public void onRemoteVideoStateChanged(String uid, int state, int reason, int elapsed) {
        for (Peer p : mPeers) {
            if (p.mUid.equals(uid)) {
                p.mHasVideo = state == RtcConstants.REMOTE_VIDEO_STATE_STARTING
                        || state == RtcConstants.REMOTE_VIDEO_STATE_DECODING;
            }
        }
        mMainHandler.post(() -> {
            for (Listener l : mListeners) {
                l.onPeerChanged();
            }
        });
    }

    @Override
    public void onNetworkQuality(String uid, int txQuality, int rxQuality) {
        Stats stats = getStats();
        stats.quality = txQuality;
    }

    @Override
    public void onLocalVideoStats(LocalVideoStats stats) {
        Stats s = getStats();
        s.sentBitrate = stats.sentBitrate;
        s.pktLoss = stats.txPacketLossRate;
        s.frameWidth = stats.encodedFrameWidth;
        s.frameHeight = stats.encodedFrameHeight;
        s.sentFrame = stats.sentFrameRate;
    }

    @Override
    public void onRemoteVideoStats(RemoteVideoStats stats) {
    }

    @Override
    public void onLocalAudioStats(LocalAudioStats stats) {
        Stats s = getStats();
        s.audioBitrate = stats.sentBitrate;
        s.audioPktLoss = stats.txPacketLossRate;
    }

    @Override
    public void onRemoteAudioStats(RemoteAudioStats stats) {
    }

    public interface State {
        int STATE_INIT = 0;
        int STATE_JOINING = 1;
        int STATE_JOINED = 2;
        int STATE_LEAVING = 3;
    }

    public static final class Peer {
        String mUid;
        boolean mHasAudio = true;
        boolean mHasVideo = true;

        public Peer(String uid) {
            mUid = uid;
        }
    }

    public static final class Stats {
        int sentBitrate = 0;
        int sentFrame = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int pktLoss = 0;
        int audioBitrate = 0;
        int audioPktLoss = 0;
        int quality = 0;
        public Stats() {}

        @SuppressLint("DefaultLocale")
        public String parseStats() {
            return String.format("local video: \nsent bitrate: %d(bps)\n", sentBitrate)
                    + String.format("sent frame rate: %d\n", sentFrame)
                    + String.format("encode resolution: %dX%d\n", frameWidth, frameHeight)
                    + String.format("packet loss rate: %d\n", pktLoss)
                    + String.format("\nlocal audio: \nsent bitrate: %d(bps)\n", audioBitrate)
                    + String.format("packet loss rate: %d\n", audioPktLoss)
                    + String.format("\nquality: %d\n", quality);
        }
    }

    public interface Listener {
        void onStateChanged(int state);
        void onPeerChanged();
    }
}
