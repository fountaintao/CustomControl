package com.taoyong.customcontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.taoyong.widget.SlideBar;

public class SlideBarActivity extends AppCompatActivity {
    private static final String TAG = "SlideBarActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_bar);
        SlideBar slideBar = (SlideBar) findViewById(R.id.slideBar);
        textView = (TextView) findViewById(R.id.textView);
        slideBar.setTextDialog(textView);
        slideBar.setSpecialEffect(SlideBar.SpecialEffect.RECTABGLE);
        //slideBar.setChooseColor(getResources().getColor(android.R.color.holo_green_light));
        //slideBar.setChoose(1);
        slideBar.addSlideListening(new SlideBar.SlideListening() {
            @Override
            public void slideChange(String c) {
                Log.d(TAG, "slideChange: " + c);
            }
        });
    }
}
