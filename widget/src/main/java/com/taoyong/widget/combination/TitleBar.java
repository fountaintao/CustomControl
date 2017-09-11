package com.taoyong.widget.combination;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taoyong.widget.R;

/**
 * Creation Time :  2017/9/11.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class TitleBar extends RelativeLayout {
    private View titleBar;
    private RelativeLayout leftGroup;
    private ImageView leftImg;
    private TextView centerTxt;
    private RelativeLayout rightGroup;
    private ImageView rightImg;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private <T extends View> T findTitleBarById(int resId) {
        return (T) titleBar.findViewById(resId);
    }

    private void init(Context context) {
        titleBar = LayoutInflater.from(context).inflate(R.layout.customcontrol_titlebar, null);
        leftGroup = findTitleBarById(R.id.customcontrol_title_bar_left_rl);
        leftImg = findTitleBarById(R.id.customcontrol_title_bar_left_iv);
        centerTxt = findTitleBarById(R.id.customcontrol_title_bar_center_tv);
        rightGroup = findTitleBarById(R.id.customcontrol_title_bar_right_rl);
        rightImg = findTitleBarById(R.id.customcontrol_title_bar_right_iv);
        leftGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickTitleBarListener) {
                    onClickTitleBarListener.onClickLeft(v);
                }
            }
        });
        rightGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickTitleBarListener) {
                    onClickTitleBarListener.onClickRight(v);
                }
            }
        });
        centerTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickTitleBarListener) {
                    onClickTitleBarListener.onClickTitle(v);
                }
            }
        });

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleBar.setLayoutParams(lp);

        addView(titleBar);
    }

    public void setTitleBarLeftImage(Object obj) throws RuntimeException {
        if (null != leftImg) {
            if (obj instanceof Integer) {
                leftImg.setImageResource((Integer) obj);
            } else if (obj instanceof Bitmap) {
                leftImg.setImageBitmap((Bitmap) obj);
            } else if (obj instanceof Drawable) {
                leftImg.setImageDrawable((Drawable) obj);
            } else if (obj instanceof Uri) {
                leftImg.setImageURI((Uri) obj);
            } else {
                throw new RuntimeException("Please pass the parameters correctly.");
            }
        } else {
            throw new RuntimeException("The control was not found.");
        }
    }

    public void setTitleBarRightImage(Object obj) throws RuntimeException {
        if (null != rightImg) {
            if (obj instanceof Integer) {
                rightImg.setImageResource((Integer) obj);
            } else if (obj instanceof Bitmap) {
                rightImg.setImageBitmap((Bitmap) obj);
            } else if (obj instanceof Drawable) {
                rightImg.setImageDrawable((Drawable) obj);
            } else if (obj instanceof Uri) {
                rightImg.setImageURI((Uri) obj);
            } else {
                throw new RuntimeException("Please pass the parameters correctly.");
            }
        } else {
            throw new RuntimeException("The control was not found.");
        }
    }

    public void setTitleBarCenterText(String text) throws RuntimeException {
        if (null != centerTxt) {
            centerTxt.setText(text);
        } else {
            throw new RuntimeException("The control was not found.");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setTitleBarBackground(Object obj) throws RuntimeException {
        if (null != titleBar) {
            if (obj instanceof Integer) {
                titleBar.setBackgroundResource((Integer) obj);
            } else if (obj instanceof Drawable) {
                titleBar.setBackground((Drawable) obj);
            } else {
                throw new RuntimeException("Please pass the parameters correctly.");
            }
        } else {
            throw new RuntimeException("The control was not found.");
        }
    }

    public ImageView getLeftImg() {
        return leftImg;
    }

    public TextView getCenterTxt() {
        return centerTxt;
    }

    public ImageView getRightImg() {
        return rightImg;
    }

    private onClickTitleBarListener onClickTitleBarListener;

    public void setOnClickTitleBarListener(TitleBar.onClickTitleBarListener onClickTitleBarListener) {
        this.onClickTitleBarListener = onClickTitleBarListener;
    }

    public interface onClickTitleBarListener {
        void onClickLeft(View v);

        void onClickRight(View v);

        void onClickTitle(View v);
    }
}