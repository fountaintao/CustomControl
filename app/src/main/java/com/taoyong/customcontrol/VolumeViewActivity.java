package com.taoyong.customcontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.taoyong.widget.VolumeView;

public class VolumeViewActivity extends AppCompatActivity {
    private static final String TAG = "VolumeViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_view);

        VolumeView volumeView = (VolumeView) findViewById(R.id.volumeView);
        volumeView.addVolumeChangeListener(new VolumeView.VolumeChangeListener() {
            @Override
            public void VolumeChange(int currentVolume) {
                Log.d(TAG, "VolumeChange: " + currentVolume);
            }
        });
    }
}
