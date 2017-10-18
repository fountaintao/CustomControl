package com.taoyong.fragment.bases;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Creation Time :  2017/10/18.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public abstract class BaseFragment extends Fragment {
    protected View view;
    protected Context context;
    protected Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(loadLayout(), null);
    }

    /**
     * 加载页面布局
     *
     * @return
     */
    protected abstract int loadLayout();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        context = getActivity();
        activity = getActivity();
        init(savedInstanceState);
    }

    /**
     * 初始化布局及其他操作
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 得到控件的方法
     *
     * @param resId 控件的ID
     * @param <T>   控件所属类
     * @return 控件
     */
    protected <T extends View> T findViewById(@IdRes int resId) {
        return (T) view.findViewById(resId);
    }

    /**
     * 提示框
     *
     * @param obj
     */
    protected void showMarked(Object obj) {
        Toast.makeText(context, "" + obj, Toast.LENGTH_SHORT).show();
    }
}
