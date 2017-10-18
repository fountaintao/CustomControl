package com.taoyong.customcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.taoyong.activities.TestFragmentActivity;
import com.taoyong.widget.combination.DelInputView;
import com.taoyong.widget.combination.TitleBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //测试DelInputView
        DelInputView delInputView = (DelInputView) findViewById(R.id.delInputView);
        delInputView.getEditText().setText("123456");
        //测试TitleBar
        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleBarCenterText("标题");
        titleBar.setTitleBarLeftImage(R.drawable.custom_control_plus);
        titleBar.setTitleBarRightImage(R.drawable.custom_control_back);
        titleBar.setTitleBarBackground(android.R.color.holo_blue_light);
        titleBar.setOnClickTitleBarListener(new TitleBar.onClickTitleBarListener() {
            @Override
            public void onClickLeft(View v) {
                toask_make("This is the picture on the left.");
            }

            @Override
            public void onClickRight(View v) {
                toask_make("Here is the picture on the right.");
            }

            @Override
            public void onClickTitle(View v) {
                toask_make("This is the title section.");
            }
        });
        //测试SlideBar
        findViewById(R.id.test_slidebar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SlideBarActivity.class));
            }
        });
        //测试RoundImageView
        findViewById(R.id.test_roundImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RoundImageViewActivity.class));
            }
        });
        //测试VolumeView
        findViewById(R.id.test_volumeView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VolumeViewActivity.class));
            }
        });
        //测试VDHLayout
        findViewById(R.id.test_vdhlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomLayoutActivity.class));
            }
        });
        //测试IncomingListener
        findViewById(R.id.test_IncomingListener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IncomingListenerActivity.class));
            }
        });
        //测试Fragment重叠问题
        findViewById(R.id.test_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestFragmentActivity.class));
            }
        });
    }

    private void toask_make(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}