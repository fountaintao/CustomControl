package com.taoyong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Creation Time :  2017/9/9.<br/>
 * Creator : tao yong.<br/>
 * Module description : <br/>
 */
public class SlideBar extends View {
    private static final String TAG = "SlideBar";

    public SlideBar(Context context) {
        super(context);
        init(context);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    //数据源
    private String[] data;
    //根据数据源的个数计算出单个字符的高度
    private int singleHeight;
    //画笔
    private Paint paint;
    //当前选中的位置
    private int choose;
    //开放一个附加的选中条件
    private boolean additionalConditions;
    //选中效果
    private SpecialEffect specialEffect;

    private void init(Context context) {
        data = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
        paint = new Paint();
        choose = -1;
        additionalConditions = true;
        specialEffect = SpecialEffect.NONE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算出可用的宽高
        int usableWidth = getWidth() - getPaddingLeft() - getPaddingRight(),
                usableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        //得到数据源的个数
        int count = data == null ? 0 : data.length;
        //根据数据源的个数计算出单个字符的高度
        singleHeight = usableHeight / count;
        //循环数据源画字符
        for (int i = 0; i < count; i++) {
            if (i == choose && additionalConditions) {
                if (specialEffect == SpecialEffect.RECTABGLE) {
                    paint.setColor(Color.parseColor("#3cff0000"));
                    paint.setAntiAlias(true);
                    canvas.drawRect(new RectF(0, singleHeight * i, usableWidth, singleHeight * (i + 1)), paint);
                    paint.reset();
                }
                paint.setColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                paint.setColor(getResources().getColor(android.R.color.black));
            }
            paint.setFakeBoldText(false);//true为粗体，false为非粗体
            paint.setAntiAlias(true);//抗锯齿
            float textSize = usableWidth / 2f >= singleHeight ? singleHeight : usableWidth / 2f;
            paint.setTextSize(textSize);
            /***********************************************************/
            float offx = usableWidth / 2 - paint.measureText(data[i]) / 2 + getPaddingLeft(),
                    offy = singleHeight * (i + 0.5f) + getPaddingTop() + paint.measureText("A") / 2;
            canvas.drawText(data[i], offx, offy, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        float y = e.getY();
        int currentChoose = (int) (y / singleHeight);
        //得到数据源的个数
        int count = data == null ? 0 : data.length;
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(getResources().getColor(android.R.color.transparent));
                choose = -1;
                invalidate();
                break;
            default:
                if (specialEffect == SpecialEffect.NONE) {
                    setBackgroundColor(Color.parseColor("#dcdcdc"));
                }
                if (choose != currentChoose) {
                    if (currentChoose >= 0 && currentChoose < count) {
                        if (null != slideListening) {
                            slideListening.slideChange(data[currentChoose]);
                        }
                        if (null != mTextDialog) {
                            mTextDialog.setText(data[currentChoose]);
                            if (!mTextDialog.isShown()) {
                                mTextDialog.setVisibility(VISIBLE);
                            }
                        }
                        choose = currentChoose;
                        invalidate();
                    }
                }
                break;
        }
        super.dispatchTouchEvent(e);
        return true;
    }

    public void setChoose(int choose) {
        this.choose = choose;
        invalidate();
    }

    public void setAdditionalConditions(boolean additionalConditions) {
        this.additionalConditions = additionalConditions;
        invalidate();
    }

    public void setSpecialEffect(SpecialEffect specialEffect) {
        this.specialEffect = specialEffect;
        invalidate();
    }

    private TextView mTextDialog;

    public void setTextDialog(TextView mTextDialog) {
        //这里简单开放一个显示字母的TextView
        this.mTextDialog = mTextDialog;
        if (mTextDialog.isShown()) {
            mTextDialog.setVisibility(GONE);
        }
    }

    private SlideListening slideListening;

    public void addSlideListening(SlideListening slideListening) {
        this.slideListening = slideListening;
    }

    public interface SlideListening {
        void slideChange(String c);
    }

    public enum SpecialEffect {
        NONE,
        RECTABGLE
    }
}