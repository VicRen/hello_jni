package ren.vic.helloaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.webrtc.SurfaceViewRenderer;

import java.util.Set;
import java.util.Timer;

public class VideoActivity extends AppCompatActivity implements EngineDelegate.Listener {

    private SurfaceViewRenderer mLocalSurface;
    private PeerView mPeer1;
    private PeerView mPeer2;

    private Button mBtnAudio;
    private Button mBtnVideo;

    private boolean mIsAudioMuted;
    private boolean mIsVideoMuted;

    private TextView mTvStats;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mLocalSurface = findViewById(R.id.local_render);
        mLocalSurface.setEnableHardwareScaler(false);
        mLocalSurface.setZOrderMediaOverlay(true);
        mPeer1 = new PeerView(findViewById(R.id.txt_peer), findViewById(R.id.remote_renderer));
        mPeer2 = new PeerView(findViewById(R.id.txt_peer2), findViewById(R.id.remote_renderer2));
        mBtnAudio = findViewById(R.id.btn_audio);
        mBtnVideo = findViewById(R.id.btn_video);
        mBtnAudio.setOnClickListener(v -> toggleAudio());
        mBtnVideo.setOnClickListener(v -> toggleVideo());
        findViewById(R.id.btn_leave).setOnClickListener(v -> doLeave());
        findViewById(R.id.btn_Stats).setOnClickListener(v -> clickStats());

        mTvStats = findViewById(R.id.tv_stats);
        mTvStats.setOnClickListener(v -> mTvStats.setVisibility(View.INVISIBLE));


        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStats();
                handler.postDelayed(this, 2000);
            }
        }, 2000);

        EngineDelegate.getInstance().addListener(this);
        EngineDelegate.getInstance().initSurfaceView(mLocalSurface);
        EngineDelegate.getInstance().initSurfaceView(mPeer1.mSurfaceView);
        EngineDelegate.getInstance().initSurfaceView(mPeer2.mSurfaceView);
        EngineDelegate.getInstance().setupLocalVideo(mLocalSurface);
        updateRender();

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EngineDelegate.getInstance().removeListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doLeave();
    }

    @Override
    public void onStateChanged(int state) {
        if (state == EngineDelegate.State.STATE_INIT) {
            finish();
        }
    }

    @Override
    public void onPeerChanged() {
        updateRender();
    }

    private void toggleAudio() {
        EngineDelegate.getInstance().toggleAudio(!mIsAudioMuted);
        mIsAudioMuted = !mIsAudioMuted;
        mBtnAudio.setText(mIsAudioMuted ? "Audio Off" : "Audio On");
    }

    private void toggleVideo() {
        EngineDelegate.getInstance().toggleVideo(!mIsVideoMuted);
        mIsVideoMuted = !mIsVideoMuted;
        mBtnVideo.setText(mIsVideoMuted ? "Video Off" : "Video On");
    }

    private void clickStats() {
        mTvStats.setVisibility(View.VISIBLE);
    }

    private void doLeave() {
        EngineDelegate.getInstance().leave();
    }

    private void updateRender() {
        mPeer1.unbindUser();
        mPeer2.unbindUser();
        Set<EngineDelegate.Peer> peerSet = EngineDelegate.getInstance().getPeers();
        for (EngineDelegate.Peer p : peerSet) {
            if (!mPeer1.isBind()) {
                mPeer1.bindUser(p.mUid, p.mHasAudio, p.mHasVideo);
            } else if (!mPeer2.isBind()) {
                mPeer2.bindUser(p.mUid, p.mHasAudio, p.mHasVideo);
            }
        }
    }

    private void updateStats() {
        mTvStats.setText(EngineDelegate.getInstance().getStats().parseStats());
    }

    private static final class PeerView {
        private final TextView mTvNote;
        private final SurfaceViewRenderer mSurfaceView;
        private String mUid = "";
        private boolean mAudio;
        private boolean mVideo;

        public PeerView(TextView note, SurfaceViewRenderer surfaceView) {
            mTvNote = note;
            mSurfaceView = surfaceView;
        }

        public void bindUser(String uid, boolean hasAudio, boolean hasVideo) {
            mUid = uid;
            mSurfaceView.setVisibility(View.VISIBLE);
            EngineDelegate.getInstance().setupRemoteVideo(mUid, mSurfaceView);
            mAudio = hasAudio;
            mVideo = hasVideo;
            updateState(mAudio, mVideo);
        }

        public void unbindUser() {
            if (!mUid.isEmpty()) {
                // TODO remove canvas
            }
            mUid = "";
            mSurfaceView.setVisibility(View.INVISIBLE);
            mTvNote.setText("");
        }

        public void updateState(boolean hasAudio, boolean hasVideo) {
            mAudio = hasAudio;
            mVideo = hasVideo;
            mTvNote.setText(String.format("uid: %s audio: %s video %s", mUid, mAudio ? "on" : "off", mVideo ? "on" : "off"));
            mSurfaceView.setVisibility(mVideo ? View.VISIBLE : View.INVISIBLE);
        }

        public boolean isBind() {
            return !mUid.equals("");
        }
    }
}