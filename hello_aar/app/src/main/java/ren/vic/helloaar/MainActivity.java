package ren.vic.helloaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements EngineDelegate.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();

    static {
        System.loadLibrary("lsrtc");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edtChannel = findViewById(R.id.edt_channel);
        Button btnJoin = findViewById(R.id.btn_join);

        EngineDelegate.getInstance().initialize(getApplicationContext());
        EngineDelegate.getInstance().addListener(this);
        btnJoin.setOnClickListener(v -> {
            String channelId = edtChannel.getText().toString();
            if (!channelId.isEmpty()) {
                EngineDelegate.getInstance().join(channelId);
            }
        });
        findViewById(R.id.btn_settings).setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStateChanged(int state) {
        if (state == EngineDelegate.State.STATE_JOINED) {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPeerChanged() {
        Log.d(TAG, "onPeerChanged: ");
    }
}