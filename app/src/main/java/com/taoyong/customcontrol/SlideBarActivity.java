package com.taoyong.customcontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taoyong.widget.SlideBar;

public class SlideBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_bar);
        SlideBar slideBar = (SlideBar) findViewById(R.id.slideBar);
    }
}
