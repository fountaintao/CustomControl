package com.taoyong.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.taoyong.customcontrol.R;
import com.taoyong.fragment.Tab1Fragment;
import com.taoyong.fragment.Tab2Fragment;
import com.taoyong.fragment.Tab3Fragment;

public class TestFragmentActivity extends AppCompatActivity {
    private static final String TAG = "TestFragmentActivity";
    private Tab1Fragment tab1Fragment;
    private Tab2Fragment tab2Fragment;
    private Tab3Fragment tab3Fragment;
    private int index = 1;
    private String[] tabs = {"index", "tab1", "tab2", "tab3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        Log.d(TAG, "onCreate: --------------------");
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt(tabs[0], 1);
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(tabs[1]);
            if (null != fragment && fragment instanceof Tab1Fragment) {
                tab1Fragment = (Tab1Fragment) fragment;
            }
            fragment = fragmentManager.findFragmentByTag(tabs[2]);
            if (null != fragment && fragment instanceof Tab2Fragment) {
                tab2Fragment = (Tab2Fragment) fragment;
            }
            fragment = fragmentManager.findFragmentByTag(tabs[3]);
            if (null != fragment && fragment instanceof Tab3Fragment) {
                tab3Fragment = (Tab3Fragment) fragment;
            }
        }
        showTab(index);
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(tabs[0], index);
        Log.d(TAG, "onSaveInstanceState: --------------------");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.d(TAG, "onAttachFragment: --------------------" + fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: --------------------");
    }

    private void init() {
        findViewById(R.id.tv_tab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(1);
            }
        });
        findViewById(R.id.tv_tab2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(2);
            }
        });
        findViewById(R.id.tv_tab3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(3);
            }
        });
    }

    private void showTab(int index) {
        this.index = index;
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 1:
                if (null == tab1Fragment) {
                    tab1Fragment = new Tab1Fragment();
                    transaction.add(R.id.fragment, tab1Fragment, tabs[index]);
                } else {
                    transaction.show(tab1Fragment);
                }
                break;
            case 2:
                if (null == tab2Fragment) {
                    tab2Fragment = new Tab2Fragment();
                    transaction.add(R.id.fragment, tab2Fragment, tabs[index]);
                } else {
                    transaction.show(tab2Fragment);
                }
                break;
            case 3:
                if (null == tab3Fragment) {
                    tab3Fragment = new Tab3Fragment();
                    transaction.add(R.id.fragment, tab3Fragment, tabs[index]);
                } else {
                    transaction.show(tab3Fragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != tab1Fragment) {
            transaction.hide(tab1Fragment);
        }
        if (null != tab2Fragment) {
            transaction.hide(tab2Fragment);
        }
        if (null != tab3Fragment) {
            transaction.hide(tab3Fragment);
        }
    }
}
