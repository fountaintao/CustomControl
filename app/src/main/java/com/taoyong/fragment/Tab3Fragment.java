package com.taoyong.fragment;

import android.os.Bundle;
import android.view.View;

import com.taoyong.customcontrol.R;
import com.taoyong.fragment.bases.BaseFragment;

/**
 * Creation Time :  2017/10/18.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class Tab3Fragment extends BaseFragment {
    /**
     * 加载页面布局
     *
     * @return
     */
    @Override
    protected int loadLayout() {
        return R.layout.fragment_tab;
    }

    /**
     * 初始化布局及其他操作
     *
     * @param savedInstanceState
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.view3).setVisibility(View.VISIBLE);
    }
}
