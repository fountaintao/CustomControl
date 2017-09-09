package com.taoyong.customcontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taoyong.widget.combination.DelInputView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DelInputView delInputView = (DelInputView) findViewById(R.id.delInputView);
        delInputView.getEditText().setText("123456");
    }
}