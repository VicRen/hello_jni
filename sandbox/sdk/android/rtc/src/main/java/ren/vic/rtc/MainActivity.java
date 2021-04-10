package ren.vic.rtc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("lsrtc");
    }

    private Handler mMainHandler;
    private Handler mWorkHandler;
    private RtcEngine rtcEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HandlerThread handlerThread = new HandlerThread("worker");
        handlerThread.start();
        mWorkHandler = new Handler(handlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);

        mWorkHandler.post(()->{
            try {
                rtcEngine = RtcEngine.create(getApplicationContext(), "", new IRtcEngineEventHandler() {
                    @Override
                    public void onWarning(int warn) {
                        super.onWarning(warn);
                    }

                    @Override
                    public void onError(int err) {
                        mMainHandler.post(()-> tv.setText("YES!"));
                    }
                });
                mMainHandler.post(()-> tv.setText("READY!"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mWorkHandler.post(()-> rtcEngine.TestingInt());
    }
}