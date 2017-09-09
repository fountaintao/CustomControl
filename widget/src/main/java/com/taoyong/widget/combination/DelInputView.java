package com.taoyong.widget.combination;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.taoyong.widget.R;

/**
 * Creation Time :  2017/9/9.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class DelInputView extends RelativeLayout implements TextWatcher, View.OnClickListener {

    public DelInputView(Context context) {
        super(context);
        init(context);
    }

    public DelInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DelInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DelInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private static final String TAG = "DelInputView";
    private Context context;
    private Integer delResId;
    private Bitmap delBitmap;
    private EditText editText;
    private ImageView imageView;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context) {
        this.context = context;
        editText = new EditText(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        editText.setBackground(null);
        addView(editText);
        editText.addTextChangedListener(this);
        if (null == getBackground()) {
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = getPaddingRight();
            int bottom = getPaddingBottom();
            setBackgroundResource(R.drawable.delinputview);
            // 这里调用了setBackgroundResource方法后
            // 会遗忘padding所以需要重新调用一下
            setPadding(left, top, right, bottom);
        }
    }

    private void createImageView() {
        imageView = new ImageView(context);
        int width = getWidth();
        int height = getHeight() / 2;
        LayoutParams lp = new LayoutParams(height, height);
        imageView.setLayoutParams(lp);
        lp.topMargin = height / 2;
        lp.leftMargin = (int) (width - height * 1.5f - getPaddingLeft());
        if (null != delResId) {
            imageView.setImageResource(delResId);
        } else if (null != delBitmap) {
            imageView.setImageBitmap(delBitmap);
        } else {
            imageView.setImageResource(R.drawable.custom_control_del);
        }
        addView(imageView);
        imageView.setOnClickListener(this);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            if (imageView.isShown()) {
                imageView.setVisibility(GONE);
            }
        } else {
            if (!imageView.isShown()) {
                imageView.setVisibility(VISIBLE);
            }
        }
    }

    private Runnable updateImageView = new Runnable() {
        @Override
        public void run() {
            if (getHeight() <= 0) {
                new Handler().postDelayed(updateImageView, 100);
            } else {
                createImageView();
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == imageView) {
            editText.setText("");
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null == imageView) {
            new Handler().postDelayed(updateImageView, 0);
        }
        if (TextUtils.isEmpty(s.toString())) {
            if (null != imageView) {
                if (imageView.isShown()) {
                    imageView.setVisibility(GONE);
                }
            }
        } else {
            if (null != imageView) {
                if (!imageView.isShown()) {
                    imageView.setVisibility(VISIBLE);
                }
            }
        }
    }

    public EditText getEditText() throws RuntimeException {
        if (null == editText)
            throw new RuntimeException("The input box hasn't been created yet .");
        return editText;
    }

    /**
     * 为删除按钮设置本地图片资源的方法
     *
     * @param resId 资源ID
     */
    public void setDelImageResource(int resId) {
        //如果没有创建删除按钮，则把资源ID赋值给全局
        //如果创建了删除按钮，则当场更改删除图片资源
        if (null == imageView) {
            delResId = resId;
        } else {
            imageView.setImageResource(resId);
        }
    }

    /**
     * 为删除按钮设置静态位图的方法
     *
     * @param bm 图片位图
     */
    public void setDelImageBitmap(Bitmap bm) {
        if (null == imageView) {
            delBitmap = bm;
        } else {
            imageView.setImageBitmap(bm);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}